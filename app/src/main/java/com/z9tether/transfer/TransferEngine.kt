package com.z9tether.transfer

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import java.io.OutputStream
import java.time.LocalDate

class TransferEngine(private val context: Context) {

    /**
     * Publishes an already-received JPEG atomically through MediaStore.
     * The PTP transport will supply the bytes in the next implementation step.
     */
    fun publishJpeg(filename: String, bytes: ByteArray): Boolean {
        val resolver = context.contentResolver
        val relative = "Pictures/Z9 Tether/${LocalDate.now()}"

        val values = ContentValues().apply {
            put(Media.DISPLAY_NAME, filename)
            put(Media.MIME_TYPE, "image/jpeg")
            put(Media.RELATIVE_PATH, "$relative/")
            put(Media.IS_PENDING, 1)
        }

        val uri = resolver.insert(Media.EXTERNAL_CONTENT_URI, values) ?: return false

        return try {
            resolver.openOutputStream(uri).use { output: OutputStream? ->
                requireNotNull(output)
                output.write(bytes)
                output.flush()
            }

            values.clear()
            values.put(Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
            true
        } catch (t: Throwable) {
            resolver.delete(uri, null, null)
            false
        }
    }
}
