package com.opentune.data.download

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat
import com.opentune.data.library.SongRef
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

/**
 * Copies of finished downloads in the phone's Music folder (Music/MTune),
 * named "Artist - Title", for other music apps to find and play. Settings ›
 * Downloads › Export compatible downloads turns it on.
 *
 * Only formats Android's Music collection takes are copied: AAC in MP4,
 * FLAC, Ogg, MP3 and WAV. The media store refuses WebM outright, so with the
 * setting on, YouTube downloads are fetched as AAC in MP4 from the start
 * (see [com.opentune.data.innertube.StreamResolver.resolveForDownload]).
 * Supports Android 10+ (scoped storage) and Android 9 and older (Music/MTune with storage permission).
 */
object DownloadExport {
    private const val TAG = "DownloadExport"

    val supported: Boolean get() = true

    val needsStoragePermission: Boolean get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

    fun hasStoragePermission(context: Context): Boolean =
        !needsStoragePermission || ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

    private val MIME = mapOf(
        "m4a" to "audio/mp4", "mp4" to "audio/mp4", "aac" to "audio/aac",
        "flac" to "audio/flac", "ogg" to "audio/ogg", "opus" to "audio/ogg",
        "mp3" to "audio/mpeg", "wav" to "audio/wav",
    )

    /** Whether [file] is in a format the Music collection takes. */
    fun compatible(file: File): Boolean = file.extension.lowercase() in MIME

    /** Adds a copy of [file] for [song] to Music/MTune; its row's URI, or null where it can't be. */
    suspend fun publish(context: Context, song: SongRef, file: File): Uri? = withContext(Dispatchers.IO) {
        if (!file.exists()) return@withContext null
        val ext = file.extension.lowercase()
        val mime = MIME[ext] ?: return@withContext null
        val fileName = com.opentune.data.Ringtones.fileName(song.toSong(), ext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val values = ContentValues().apply {
                put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Audio.Media.TITLE, song.title)
                put(MediaStore.Audio.Media.ARTIST, song.artist)
                song.albumName?.takeIf { it.isNotBlank() }?.let { put(MediaStore.Audio.Media.ALBUM, it) }
                put(MediaStore.Audio.Media.MIME_TYPE, mime)
                put(MediaStore.Audio.Media.IS_MUSIC, 1)
                put(MediaStore.Audio.Media.RELATIVE_PATH, "${Environment.DIRECTORY_MUSIC}/MTune")
                put(MediaStore.Audio.Media.IS_PENDING, 1)
            }
            val uri = runCatching { resolver.insert(collection, values) }
                .onFailure { Log.w(TAG, "Couldn't add ${song.videoId} to the Music folder", it) }
                .getOrNull() ?: return@withContext null
            runCatching {
                resolver.openOutputStream(uri)?.use { out -> file.inputStream().use { it.copyTo(out) } } ?: error("no stream")
                resolver.update(uri, ContentValues().apply { put(MediaStore.Audio.Media.IS_PENDING, 0) }, null, null)
                uri
            }.getOrElse {
                Log.w(TAG, "Couldn't write ${song.videoId} to the Music folder", it)
                runCatching { resolver.delete(uri, null, null) }
                null
            }
        } else {
            // Android 9 (API 28) and older
            if (!hasStoragePermission(context)) {
                Log.w(TAG, "Cannot export ${song.videoId}: storage permission not granted on Android ${Build.VERSION.SDK_INT}")
                return@withContext null
            }
            runCatching {
                @Suppress("DEPRECATION")
                val musicDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "MTune")
                if (!musicDir.exists()) {
                    musicDir.mkdirs()
                }
                val destFile = File(musicDir, fileName)
                file.copyTo(destFile, overwrite = true)

                // Scan file to register with MediaStore and obtain content Uri so other music apps see it
                val scannedUri = withTimeoutOrNull(4000L) {
                    suspendCancellableCoroutine<Uri?> { cont ->
                        android.media.MediaScannerConnection.scanFile(
                            context,
                            arrayOf(destFile.absolutePath),
                            arrayOf(mime)
                        ) { _, uri ->
                            if (cont.isActive) cont.resume(uri)
                        }
                    }
                }
                scannedUri ?: Uri.fromFile(destFile)
            }.onFailure {
                Log.w(TAG, "Couldn't save ${song.videoId} to Music/MTune on Android ${Build.VERSION.SDK_INT}", it)
            }.getOrNull()
        }
    }

    /** Takes the copy at [uri] back out of the Music folder. */
    suspend fun unpublish(context: Context, uri: String) = withContext(Dispatchers.IO) {
        val parsedUri = runCatching { Uri.parse(uri) }.getOrNull() ?: return@withContext
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            runCatching {
                if (parsedUri.scheme == "file") {
                    parsedUri.path?.let { File(it).takeIf { f -> f.exists() }?.delete() }
                } else if (parsedUri.scheme == "content") {
                    context.contentResolver.query(
                        parsedUri,
                        arrayOf(MediaStore.MediaColumns.DATA),
                        null, null, null
                    )?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val path = cursor.getString(0)
                            if (!path.isNullOrBlank()) {
                                File(path).takeIf { it.exists() }?.delete()
                            }
                        }
                    }
                }
            }.onFailure { Log.w(TAG, "Couldn't delete local file for $uri", it) }
        }
        runCatching { context.contentResolver.delete(parsedUri, null, null) }
            .onFailure { Log.w(TAG, "Couldn't remove $uri from the Music folder", it) }
    }
}
