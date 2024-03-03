package com.example.parentcontrolapp.utils

import android.content.Context
import android.content.pm.PackageManager
import com.example.parentcontrolapp.model.InstalledApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getInstalledApps(ctx: Context): ArrayList<InstalledApp> = withContext(Dispatchers.Default) {
    val packageManager = ctx.packageManager
    val apps = ArrayList<InstalledApp>()

    val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
    val usageApps = if (getUsageStatsPermission(ctx)) {
        fetchAppScreenTime(ctx)
    } else {
        askUsageStatsPermission(ctx)
        emptyList()
    }

    for (app in installedApps) {
        val label = packageManager.getApplicationLabel(app).toString()

        if (!isSystem(label, app)) {
            val icon = convertToBitmap(packageManager.getApplicationIcon(app.packageName))
            val usage = usageApps.find {
                it.packageName == app.packageName
            }

            apps.add(
                InstalledApp(
                    name = label,
                    packageName = app.packageName,
                    icon = icon,
                    screenTime = usage?.foreGroundTime ?: 0,
                )
            )
        }
    }

    return@withContext apps
}