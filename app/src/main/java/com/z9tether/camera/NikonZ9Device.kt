package com.z9tether.camera

import android.hardware.usb.UsbDevice
import com.z9tether.usb.UsbCameraManager

class NikonZ9Device(
    private val usb: UsbCameraManager
) {
    fun isZ9Candidate(device: UsbDevice): Boolean =
        usb.isLikelyNikon(device) && usb.ptpInterface(device) != null
}
