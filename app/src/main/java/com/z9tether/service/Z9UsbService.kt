package com.z9tether.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.z9tether.usb.UsbCameraManager

class Z9UsbService : Service() {

    override fun onCreate() {
        super.onCreate()
        createChannel()
        startForeground(NOTIFICATION_ID, notification("USB tether service ready"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_SCAN) {
            val manager = UsbCameraManager(this)
            val nikon = manager.devices().firstOrNull(manager::isLikelyNikon)
            val text = if (nikon != null) {
                "Nikon USB device detected"
            } else {
                "Waiting for Nikon Z9"
            }
            getSystemService(NotificationManager::class.java)
                .notify(NOTIFICATION_ID, notification(text))
        }
        return START_STICKY
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Z9 USB Tether",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun notification(text: String): Notification =
        Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Z9 Tether")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.stat_sys_data_usb)
            .setOngoing(true)
            .build()

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_SCAN = "com.z9tether.SCAN"
        private const val CHANNEL_ID = "z9_usb"
        private const val NOTIFICATION_ID = 9001
    }
}
