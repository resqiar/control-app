package com.example.parentcontrolapp.utils

import android.app.usage.UsageStatsManager
import android.content.Context
import com.example.parentcontrolapp.model.AppUsage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

suspend fun fetchAppScreenTime(ctx: Context): ArrayList<AppUsage> = withContext(Dispatchers.Default) {
    val usageStatsManager = ctx.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val calendar = Calendar.getInstance()
    val apps = ArrayList<AppUsage>()

    // last 24 hours
    calendar.add(Calendar.DAY_OF_MONTH, -1)

    val stats = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        calendar.timeInMillis,
        System.currentTimeMillis()
    )

    if (stats != null && stats.isNotEmpty()) {
        for (app in stats) {
            if (!isSystemByPackageName(ctx, app.packageName)) {
                val name = app.packageName
                val totalTime = app.totalTimeInForeground / (60 * 1000)

                apps.add(AppUsage(
                    packageName = name,
                    foreGroundTime = totalTime
                ))
            }
        }
    }

    return@withContext apps
}