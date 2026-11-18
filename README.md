# MTune

**MTune** is a rebranded, full-featured open-source Android music streaming and audiophile playback application **based on [OpenTune](https://github.com/justshivv/Opentune)** under the **GNU General Public License v3.0 (GPL-3.0)**.

## Features

Includes the complete OpenTune feature set rebranded as **MTune** (`com.mtune.app`, `com.mtune.tv`, `com.mtune.wear`):
- **YouTube Music Streaming**: Dual stream engine (`InnerTubeX` + multi-client fallback + `NewPipeExtractor`), Google account sign-in, Home/Explore/Search/Library, AutoPlay radio.
- **BitChord-Compatible Lossless Add-ons & JioSaavn 320 kbps Fallback**: Parallel racing, strict track matching, binary 42-byte FLAC header verification, DRM rejection, and seamless 160 ms silent-bridge live lossless upgrades.
- **Audiophile Audio Engine**: 32-bit float TDF-II 15-band EQ, AutoEq headphone correction, Clarity, Space3D, TPDF dither, BPM/key Automix crossfade, Dolby Atmos session integration, and 4-tier bit-perfect output (C++ `usbfs` USB DAC driver, C++ AAudio Exclusive MMAP, Android 14+ Bit-Perfect USB, AudioTrack).
- **Offline Downloads, Lyrics & Ecosystem**: WorkManager ranged downloads + SmartDownloads + `Music/MTune` MediaStore export, multi-provider synced/word-level lyrics, Listen Together (Nostr), Spotify import & Canvas, Subsonic/Navidrome, Last.fm, ListenBrainz, Discord RPC, Podcasts, Radio Browser, Song Recognition, Android Auto, Home Screen Widgets, and Android TV / Wear OS apps.

## Fork Modifications (MTune)
- Rebranded app name, UI labels, adaptive launcher icons, Android Auto label, Discord/ListenBrainz/Subsonic client names, and `Music/MTune` MediaStore export folder to **MTune**.
- Changed Android application IDs to `com.mtune.app`, `com.mtune.tv`, and `com.mtune.wear` (installable side-by-side with upstream OpenTune).
- Disabled upstream OpenTune forced `UpdateLock` and upstream analytics pings (`Usage.kt`).
- Added automated GitHub Actions release workflow (`.github/workflows/build-mtune-apk.yml`) producing signed `MTune-universal.apk`, ABI-specific APKs (`arm64-v8a`, `armeabi-v7a`, `x86_64`), and `MTune-TV.apk`.

## Building & Downloading the APK

Push to `main` (or tag `v*` / trigger **Build MTune APK** manually in the **Actions** tab) to automatically build and publish `MTune-universal.apk`, `MTune-arm64-v8a.apk`, `MTune-armeabi-v7a.apk`, `MTune-x86_64.apk`, and `MTune-TV.apk` to **Releases** and **Artifacts**.

## License & Attribution

Licensed under **GNU GPL-3.0** — see [LICENSE](LICENSE) and [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).  
Based on [OpenTune](https://github.com/justshivv/Opentune) by justshivv and contributors.
