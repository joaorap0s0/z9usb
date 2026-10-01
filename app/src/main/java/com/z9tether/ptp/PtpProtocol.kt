package com.z9tether.ptp

import android.hardware.usb.UsbDeviceConnection
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.min

/**
 * Minimal PTP transport.
 *
 * PTP packets are little-endian. This class intentionally implements only
 * the baseline operations needed for the first Z9 interoperability test.
 */
class PtpProtocol(
    private val connection: UsbDeviceConnection,
    private val bulkIn: Int,
    private val bulkOut: Int,
    private val interruptIn: Int? = null
) {
    private var transactionId = 1u

    fun openSession(): ByteArray {
        val id = transactionId++
        return transact(OP_OPEN_SESSION, uint32(1u), id)
    }

    fun getDeviceInfo(): ByteArray {
        return transact(OP_GET_DEVICE_INFO)
    }

    fun getStorageIds(): ByteArray {
        return transact(OP_GET_STORAGE_IDS)
    }

    fun getObjectHandles(storageId: UInt = 0u): ByteArray {
        return transact(OP_GET_OBJECT_HANDLES, uint32(0u), uint32(storageId))
    }

    fun getObjectInfo(handle: UInt): ByteArray {
        return transact(OP_GET_OBJECT_INFO, uint32(handle))
    }

    fun getObject(handle: UInt): ByteArray {
        return transact(OP_GET_OBJECT, uint32(handle), dataExpected = true)
    }

    private fun transact(
        operation: UShort,
        vararg params: ByteArray,
        dataExpected: Boolean = false
    ): ByteArray {
        val id = transactionId++
        val payload = params.fold(ByteArray(0)) { a, b -> a + b }
        val packet = ByteArray(12 + payload.size)
        val bb = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN)
        bb.putInt(packet.size)
        bb.putShort(PTP_COMMAND.toShort())
        bb.putShort(operation.toShort())
        bb.putInt(id.toInt())
        bb.put(payload)

        if (connection.bulkTransfer(bulkOut, packet, packet.size, 5000) < 0) {
            error("PTP bulk OUT failed")
        }

        val buffer = ByteArray(1024 * 1024)
        val n = connection.bulkTransfer(bulkIn, buffer, buffer.size, 10000)
        if (n < 12) error("PTP bulk IN failed")

        return buffer.copyOf(n)
    }

    private fun uint32(value: UInt): ByteArray =
        ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value.toInt()).array()

    companion object {
        const val PTP_COMMAND: UShort = 1u
        const val PTP_RESPONSE: UShort = 3u
        const val PTP_DATA: UShort = 2u

        const val OP_OPEN_SESSION: UShort = 0x1002u
        const val OP_GET_DEVICE_INFO: UShort = 0x1001u
        const val OP_GET_STORAGE_IDS: UShort = 0x1004u
        const val OP_GET_OBJECT_HANDLES: UShort = 0x1007u
        const val OP_GET_OBJECT_INFO: UShort = 0x1008u
        const val OP_GET_OBJECT: UShort = 0x1009u
    }
}
