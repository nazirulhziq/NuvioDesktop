<div align="center">

# Nuvio Desktop Enhanced

**A desktop build of Nuvio for Windows and Linux — tuned player, Live TV, and a floating dock that stays out of the way.**

</div>

---

## Download

| Platform | Package | Version |
| --- | --- | --- |
| **Windows 10/11 x64** | [Nuvio.Desktop.Enhanced-v6.2.1.msi](https://github.com/nazirulhziq/NuvioDesktop/releases/download/v6.2.1/Nuvio.Desktop.Enhanced-v6.2.1.msi) | 6.2.1 |
| **Linux x64** (Debian/Ubuntu/Zorin 18+) | [nuvio_6.2.3_amd64.deb](https://github.com/nazirulhziq/NuvioDesktop/releases/download/v6.2.3/nuvio_6.2.3_amd64.deb) | 6.2.3 |

All builds: **[Releases](https://github.com/nazirulhziq/NuvioDesktop/releases)**

### Install

**Windows** — run the `.msi`. It upgrades over an existing install; no uninstall needed.

**Linux**
```bash
sudo apt remove nuvio-desktop-enhanced   # only if you installed the earlier 6.2.x package
sudo apt install ./nuvio_6.2.3_amd64.deb
```

Requires Ubuntu 24.04-era libraries (`libwebkit2gtk-4.1-0`, `libmpv2`). Zorin OS 18+ and Linux Mint 22+ qualify; 22.04-based distributions do not.

---

## What's in it

### Navigation

- **Floating bottom dock** — icon-only bar that expands with labels on hover.
- **Profile first** — the switcher sits at the left edge; tapping it opens a dropdown above the dock.
- **Settings tab** in the dock; shuffle lives inside Settings.
- **Top-left shutdown button** with a hover tooltip and the standard exit confirmation.

### Player

- **Anime4K shaders** — Fast, Sharp HQ and Line Recovery, switchable in the player.
- **Motion smoothing** — real-time frame interpolation, per media type.
- **Dual subtitles** with per-language grouping: pick the language, then pick the exact track inside it.
- **Subtitle styling** — colours, outline effects, background box, ten bundled fonts plus custom font import.
- **Language flags** in the subtitle rail — real flag images, not text codes.
- **Seek previews**, playback statistics overlay, buffered-range display, and hardware keyboard shortcuts.
- **Night mode**, eye filter, stream fallback, and a docked mini player.
- **Live network and stream stats** — speed, seeders and peers while playing.

### Live TV

- **M3U / M3U8 playlists** from URL or local file.
- **Xtream and Stalker Portal** providers.
- **XMLTV EPG** — now/next in the channel list and player, plus a full per-channel guide.
- **Channel favourites, recents, search and category filtering.**

### Library and discovery

- **Profiles** with separate watch progress, avatars and sync.
- **Continue Watching**, calendar, collections and custom posters.
- **Random picker** with filters.
- **Trakt, SIMKL, MDBList and AniList** integrations.
- **Addons, plugins and media servers** (Jellyfin, Emby, Plex, Silo).

### Desktop details

- **In-app updater** pointed at this repository's releases.
- **Nuvio account sign-in** against the public Nuvio backend.
- **Discord rich presence**, keyboard and touchscreen navigation, and controller support.

---

## Build from source

Requirements: JDK 21, Android SDK (for `sdk.dir` in `local.properties`), and a `local.properties` containing your runtime config:

```properties
sdk.dir=/path/to/Android/sdk
NUVIO_SUPABASE_URL=https://api.nuvio.tv
NUVIO_SUPABASE_ANON_KEY=<publishable key>
NUVIO_SUPABASE_FALLBACK_URL=https://api-two.nuvioapp.space
```

**Windows MSI**

```powershell
.\gradlew.bat :composeApp:packageReleaseMsi -Pcompose.desktop.packaging.checkJdkVendor=false
```

**Linux**

```bash
bash ./scripts/linux/install-desktop-build-dependencies.sh
./gradlew :composeApp:packageReleaseDeb -Pcompose.desktop.packaging.checkJdkVendor=false
```

Prebuilt CI works on demand:

- `.github/workflows/windows-msi.yml`
- `.github/workflows/linux-build.yml`

Both are `workflow_dispatch` — pick the branch, run, download the artifact.

---

## Notes

- **Linux AppImage is not built.** The packaging script expects an icon name derived from the product name; the Debian package is the supported Linux artifact.
- **Upgrading from 6.2.x Linux:** the package was renamed from `nuvio-desktop-enhanced` to `nuvio`, so remove the old package once.
- **Windows and Linux builds track different versions** right now (6.2.1 / 6.2.3). The Linux build carries the launcher-path fix.

---

## Credits

Built on [Nuvio](https://github.com/NuvioMedia/NuvioDesktop) by the Nuvio team, with [mpv](https://github.com/mpv-player/mpv) for playback and [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform) for the UI.

This is an unofficial build. Report issues here, not upstream.
