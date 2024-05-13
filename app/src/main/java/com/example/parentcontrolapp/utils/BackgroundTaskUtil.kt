package com.example.parentcontrolapp.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.parentcontrolapp.utils.api.getCurrentDeviceMetadata
import com.example.parentcontrolapp.utils.api.initListenMQ
import com.example.parentcontrolapp.utils.api.sendApplicationDataWithDeviceData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object BackgroundTaskUtil {
    private var isBackgroundRunning = false

    fun startBackgroundTask(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            // initiate RabbitMQ connection
            initListenMQ(context)
            Log.d("RabbitMQ Status", "Connected")
        }

        if (!isBackgroundRunning) {
            isBackgroundRunning = true
            val handler = Handler(Looper.getMainLooper())
            val delayMillis: Long = 60 * 1000 // 1 minute interval

            val runnable = object : Runnable {
                override fun run() {
                    CoroutineScope(Dispatchers.IO).launch {
                        getCurrentDeviceMetadata(context)
                        sendApplicationDataWithDeviceData(context)
                    }
                    handler.postDelayed(this, delayMillis)
                }
            }

            handler.post(runnable)
        }
    }
}