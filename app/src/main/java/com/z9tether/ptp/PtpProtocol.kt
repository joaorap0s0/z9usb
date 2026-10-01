package com.z9tether.ptp

import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint

class PtpProtocol(
    private val connection: UsbDeviceConnection,
    private val endpointIn: UsbEndpoint,
    private val endpointOut: UsbEndpoint
) {

    fun sendCommand(data: ByteArray, timeout: Int = 1000): Int {
        return connection.bulkTransfer(endpointOut, data, data.size, timeout)
    }

    fun readResponse(buffer: ByteArray, timeout: Int = 1000): Int {
        return connection.bulkTransfer(endpointIn, buffer, buffer.size, timeout)
    }
}
