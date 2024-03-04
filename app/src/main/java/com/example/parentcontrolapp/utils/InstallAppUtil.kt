package com.example.parentcontrolapp.utils

import android.content.Context
import android.content.pm.PackageManager
import com.example.parentcontrolapp.model.InstalledApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getInstalledApps(ctx: Context): ArrayList<InstalledApp> = withContext(Dispatchers.Default) {
    val packageManager = ctx.packageManager
    var apps = ArrayList<InstalledApp>()

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
            val time = convertToHours(usage?.foreGroundTime ?: 0)

            apps.add(
                InstalledApp(
                    name = label,
                    packageName = app.packageName,
                    icon = icon,
                    rawTime = usage?.foreGroundTime ?: 0,
                    screenTime = time,
                )
            )
        }
    }

    // sort the apps data based on screen time
    apps = ArrayList(apps.sortedByDescending {
        it.rawTime
    })

    return@withContext apps
}