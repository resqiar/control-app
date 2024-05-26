package com.resqiar.sendigi.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.resqiar.sendigi.R
import com.resqiar.sendigi.utils.api.initListenMQ
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RabbitMQService: Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        val channelId = "sendigi_rabbitmq"
        val channelName = "SenDigi Realtime Sync"
        val channelDescription = "SenDigi Realtime Sync"
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = channelDescription
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val ctx = this

        serviceScope.launch {
            // start rabbitmq
            initListenMQ(ctx)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = "sendigi_rabbitmq"
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_sendigi)
            .setContentTitle("SenDigi")
            .setContentText("Listening for Real Time Command")
            .build()

        startForeground(1, notification)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
