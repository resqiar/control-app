package com.example.parentcontrolapp.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.example.parentcontrolapp.model.InstalledApp

fun getInstalledApps(ctx: Context): ArrayList<InstalledApp> {
    val packageManager = ctx.packageManager
    val apps = ArrayList<InstalledApp>()
    val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

    for (app in installedApps) {
        if (!isSystem(app)) {
            val label = packageManager.getApplicationLabel(app).toString()
            val icon = convertToBitmap(packageManager.getApplicationIcon(app.packageName))

            apps.add(
                InstalledApp(
                    name = label,
                    packageName = app.packageName,
                    icon = icon,
                )
            )
        }
    }

    return apps
}

private fun isSystem(app: ApplicationInfo): Boolean {
    return app.flags and ApplicationInfo.FLAG_SYSTEM != 0
}

private fun convertToBitmap(icon: Drawable): ImageBitmap {
    return icon.toBitmap().asImageBitmap()
}
