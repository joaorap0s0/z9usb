package com.z9tether

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.z9tether.usb.UsbCameraManager

class MainActivity : ComponentActivity() {

    private lateinit var usbManager: UsbManager
    private val permissionAction = "com.z9tether.USB_PERMISSION"

    private var status by mutableStateOf("USB ● DISCONNECTED")

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == permissionAction) {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                val granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                status = if (granted && device != null) {
                    "USB ● PERMISSION GRANTED\n${device.deviceName}"
                } else {
                    "USB ● PERMISSION DENIED"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager

        registerReceiver(
            receiver,
            IntentFilter(permissionAction),
            Context.RECEIVER_NOT_EXPORTED
        )

        setContent {
            MaterialTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text("Z9 TETHER", style = MaterialTheme.typography.headlineMedium)
                    Text(status)
                    Text("Camera: Nikon Z9 / Nikon USB device")
                    Text("Protocol: MTP/PTP")
                    Button(onClick = { scan() }) {
                        Text("CONNECT")
                    }
                    Text(
                        "MVP: USB detection and PTP foundation. " +
                            "Physical Z9 validation is required before claiming automatic photo pull."
                    )
                }
            }
        }
    }

    private fun scan() {
        val manager = UsbCameraManager(this)
        val device = manager.devices().firstOrNull(manager::isLikelyNikon)
        if (device == null) {
            status = "USB ● Nikon device not found"
            return
        }

        if (!usbManager.hasPermission(device)) {
            val pi = PendingIntent.getBroadcast(
                this,
                0,
                Intent(permissionAction),
                PendingIntent.FLAG_IMMUTABLE
            )
            usbManager.requestPermission(device, pi)
        } else {
            status = "USB ● READY\n${device.deviceName}"
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}
