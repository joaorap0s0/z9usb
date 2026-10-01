package com.z9tether.usb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import com.z9tether.service.Z9UsbService

class UsbAttachReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
            context.startForegroundService(
                Intent(context, Z9UsbService::class.java)
                    .setAction(Z9UsbService.ACTION_SCAN)
            )
        }
    }
}
