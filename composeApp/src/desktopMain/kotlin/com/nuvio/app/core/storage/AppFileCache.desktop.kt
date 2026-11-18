package com.nuvio.app.core.storage

import java.nio.file.Files
import java.nio.file.Path

internal actual object AppFileCache {
    private fun resolve(path: String): Path? {
        val root = DesktopStorage.cacheDir.toAbsolutePath().normalize()
        val candidate = root.resolve(path).normalize()
        return candidate.takeIf { it.startsWith(root) }
    }

    actual fun read(path: String): String? {
        val file = resolve(path) ?: return null
        if (!Files.isRegularFile(file)) return null
        return runCatching { Files.readString(file) }.getOrNull()
    }

    actual fun write(path: String, payload: String) {
        val file = resolve(path) ?: return
        runCatching {
            file.parent?.let { Files.createDirectories(it) }
            Files.writeString(file, payload)
        }
    }
}
