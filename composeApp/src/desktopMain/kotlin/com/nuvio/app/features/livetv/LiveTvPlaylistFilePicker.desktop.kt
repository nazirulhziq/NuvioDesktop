package com.nuvio.app.features.livetv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
internal actual fun rememberLiveTvPlaylistFilePicker(
    onPlaylistLoaded: (fileName: String?, content: String) -> Unit,
    onError: (String) -> Unit,
): LiveTvPlaylistFilePicker {
    val scope = rememberCoroutineScope()
    return remember(onPlaylistLoaded, onError) {
        LiveTvPlaylistFilePicker(canPickFiles = true) {
            scope.launch {
                val picked = withContext(Dispatchers.IO) {
                    runCatching {
                        val dialog = FileDialog(null as Frame?, "Import playlist", FileDialog.LOAD).apply {
                            setFilenameFilter { _, name ->
                                val lower = name.lowercase()
                                lower.endsWith(".m3u") || lower.endsWith(".m3u8") || lower.endsWith(".txt")
                            }
                            isVisible = true
                        }
                        val name = dialog.file ?: return@runCatching null
                        name to File(dialog.directory, name).readText()
                    }
                }
                picked.fold(
                    onSuccess = { result ->
                        if (result != null) onPlaylistLoaded(result.first, result.second)
                    },
                    onFailure = { onError(it.message ?: "Could not read that playlist") },
                )
            }
        }
    }
}
