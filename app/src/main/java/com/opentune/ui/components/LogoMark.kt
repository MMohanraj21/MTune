package com.opentune.ui.components

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.PathParser

/**
 * The MTune mark as path data in the launcher icon's 108 by 108 space,
 * matching res/drawable/ic_launcher_foreground.xml for splash, pull-to-refresh,
 * song recognition, and share posters.
 */
internal const val MARK_VIEWPORT = 108f

internal val MARK_PATH_DATA: String =
    "M28,74 L28,36 C28,33.8 29.8,32 32,32 L36,32 C37.6,32 39.1,32.9 39.8,34.4 " +
    "L54,60 L68.2,34.4 C68.9,32.9 70.4,32 72,32 L76,32 C78.2,32 80,33.8 80,36 " +
    "L80,74 C80,76.2 78.2,78 76,78 L72,78 C69.8,78 68,76.2 68,74 L68,51 " +
    "L57.5,69.5 C56.7,70.9 55.4,71.8 54,71.8 C52.6,71.8 51.3,70.9 50.5,69.5 " +
    "L40,51 L40,74 C40,76.2 38.2,78 36,78 L32,78 C29.8,78 28,76.2 28,74 Z " +
    "M54,22 A4,4 0 1,0 54,30 A4,4 0 1,0 54,22 Z"

/** The mark as a [Path] in [MARK_VIEWPORT] units. */
internal fun markPath(): Path = PathParser().parsePathString(MARK_PATH_DATA).toPath()
