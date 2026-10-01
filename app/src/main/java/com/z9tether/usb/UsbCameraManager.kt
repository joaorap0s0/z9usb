package com.z9tether.usb

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbInterface
import android.hardware.usb.UsbManager

class UsbCameraManager(private val context: Context) {
    private val usbManager =
        context.getSystemService(Context.USB_SERVICE) as UsbManager

    fun devices(): List<UsbDevice> = usbManager.deviceList.values.toList()

    fun isLikelyNikon(device: UsbDevice): Boolean {
        // Nikon's USB vendor ID is 0x04B0 (1200 decimal).
        return device.vendorId == NIKON_VENDOR_ID
    }

    fun ptpInterface(device: UsbDevice): UsbInterface? {
        for (i in 0 until device.interfaceCount) {
            val intf = device.getInterface(i)
            // PTP still-image class: 0x06 / subclass 0x01 / protocol 0x01.
            if (intf.interfaceClass == 0x06 &&
                intf.interfaceSubclass == 0x01 &&
                intf.interfaceProtocol == 0x01
            ) return intf
        }
        return null
    }

    fun open(device: UsbDevice, intf: UsbInterface): UsbDeviceConnection? {
        val connection = usbManager.openDevice(device) ?: return null
        if (!connection.claimInterface(intf, true)) {
            connection.close()
            return null
        }
        return connection
    }

    companion object {
        const val NIKON_VENDOR_ID = 0x04B0
    }
}
