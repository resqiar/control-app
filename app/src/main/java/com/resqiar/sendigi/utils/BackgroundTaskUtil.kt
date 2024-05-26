package com.resqiar.sendigi.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.resqiar.sendigi.utils.api.getCurrentDeviceMetadata
import com.resqiar.sendigi.utils.api.sendApplicationDataWithDeviceData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object BackgroundTaskUtil {
    private var isBackgroundRunning = false

    fun startBackgroundTask(context: Context) {
        if (!isBackgroundRunning) {
            isBackgroundRunning = true
            val handler = Handler(Looper.getMainLooper())
            val delayMillis: Long = 60 * 3000 // 3 minute interval

            val runnable = object : Runnable {
                override fun run() {
                    CoroutineScope(Dispatchers.IO).launch {
                        // check internet connection first before sending state to remote server
                        // only run when there is an internet connection
                        if (checkDeviceConnection(context)) {
                            getCurrentDeviceMetadata(context)
                            sendApplicationDataWithDeviceData(context)
                        }
                    }
                    handler.postDelayed(this, delayMillis)
                }
            }

            handler.post(runnable)
        }
    }
}