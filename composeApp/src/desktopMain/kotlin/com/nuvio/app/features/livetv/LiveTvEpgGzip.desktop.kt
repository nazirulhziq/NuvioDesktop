package com.nuvio.app.features.livetv

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream

internal actual fun liveTvGunzip(bytes: ByteArray, maxOutputBytes: Int): ByteArray {
    val limit = maxOutputBytes.coerceAtLeast(0)
    GZIPInputStream(ByteArrayInputStream(bytes)).use { input ->
        val output = ByteArrayOutputStream(minOf(limit, bytes.size * 8).coerceAtLeast(1024))
        val buffer = ByteArray(16 * 1024)
        var total = 0
        while (true) {
            val read = input.read(buffer)
            if (read <= 0) break
            total += read
            if (total > limit) throw IllegalStateException("Live TV guide exceeds the ${limit} byte limit")
            output.write(buffer, 0, read)
        }
        return output.toByteArray()
    }
}
