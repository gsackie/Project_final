package com.example.project_final

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat

class CheckoutBackgroundService : Service() {

    private val handler = Handler()
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Place your logic for continuous checking of delivery time here
        handler.postDelayed({
            // Example: Check if delivery time is over
            if (isDeliveryTimeOver()) {
                showDeliveryTimeOverNotification()
            }

            // Repeat the checking every 10 seconds (adjust as needed)
            handler.postDelayed({
                onStartCommand(intent, flags, startId)
            }, 10000)
        }, 1000)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop any ongoing tasks or cleanup if needed
        handler.removeCallbacksAndMessages(null)
    }

    // Example: Check if delivery time is over (dummy implementation)
    private fun isDeliveryTimeOver(): Boolean {
        // Replace this with your actual logic for checking if delivery time is over
        return false
    }

    // Example: Show notification for delivery time over
    private fun showDeliveryTimeOverNotification() {
        val builder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Delivery Time Over")
            .setContentText("Your order delivery time is over!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(2, builder.build())
    }
}
