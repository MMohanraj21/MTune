# MTune

**MTune** is an open-source audiophile music streaming player for Android (`com.mtune.app`), Android TV (`com.mtune.tv`), and Wear OS (`com.mtune.wear`), maintained by **[@MMohanraj21](https://github.com/MMohanraj21)**.

## Highlights

- **YouTube Music Streaming & Discovery**: Home feeds, search, albums, artists, playlists, Google account sign-in, and smart AutoPlay radio with dual stream resolution.
- **Lossless Audio Add-ons**: BitChord-compatible HTTP lossless sources with binary 42-byte FLAC header validation, strict track matching, DRM rejection, and seamless 160 ms silent-bridge upgrades, plus JioSaavn 320 kbps fallback.
- **Audiophile DSP & Bit-Perfect Output**: 32-bit float TDF-II 15-band EQ, AutoEq headphone correction, Clarity, Space3D, TPDF dither, BPM/key Automix crossfade, Dolby Atmos session integration, and 4-tier bit-perfect output (direct C++ `usbfs` USB DAC driver, C++ AAudio Exclusive MMAP, Android 14+ Bit-Perfect USB, AudioTrack).
- **Offline Downloads, Lyrics & Ecosystem**: WorkManager ranged downloads + SmartDownloads + `Music/MTune` export, multi-provider word-synced lyrics, Listen Together (Nostr), Spotify import & Canvas, Subsonic/Navidrome, Last.fm, ListenBrainz, Discord RPC, Podcasts, Radio Browser, Song Recognition, Android Auto, and Home Screen Widgets.

## Downloads

Get the latest signed APKs directly from **[Releases](https://github.com/MMohanraj21/MTune/releases)**:
- **`MTune-arm64-v8a.apk`** — Modern 64-bit Android phones (recommended)
- **`MTune-universal.apk`** — Runs on all Android architectures
- **`MTune-TV.apk`** — Android TV / Google TV

## License & Acknowledgments

MTune is free software licensed under the **GNU General Public License v3.0 (GPL-3.0)** — see [LICENSE](LICENSE) and [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).  
Based on [OpenTune](https://github.com/justshivv/Opentune) and its contributors.
