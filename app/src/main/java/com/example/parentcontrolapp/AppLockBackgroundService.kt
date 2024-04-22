package com.example.parentcontrolapp// com.example.parentcontrolapp.AppLockBackgroundService.kt

import AppLockViewModel
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class AppLockBackgroundService : Service() {

    private var isServiceRunning = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isServiceRunning) {
            startMonitoring()
            isServiceRunning = true
        }
        return START_STICKY
    }

    private fun startMonitoring() {
        Thread {
            while (isServiceRunning) {
                val foregroundAppPackageName = getForegroundAppPackageName(this)
                if (isAppLocked(this, foregroundAppPackageName)) {
                    showToast("The app is locked and cannot be accessed.")
                }
                Thread.sleep(1000)
            }
        }.start()
    }

    private fun getForegroundAppPackageName(context: Context): String {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
        val currentTime = System.currentTimeMillis()
        val stats = usageStatsManager?.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 10, currentTime)

        stats?.let {
            var topActivity: String? = null
            var topTime = 0L
            for (usageStats in it) {
                if (usageStats.lastTimeUsed > topTime) {
                    topTime = usageStats.lastTimeUsed
                    topActivity = usageStats.packageName
                }
            }
            return topActivity ?: ""
        }
        return ""
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isAppLocked(context: Context, packageName: String): Boolean {
        val viewModel = AppLockViewModel() // Create an instance of your ViewModel
        return viewModel.isAppLocked(context, packageName)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
    }
}
