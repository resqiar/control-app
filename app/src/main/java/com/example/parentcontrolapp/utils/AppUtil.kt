package com.example.parentcontrolapp.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

fun isSystem(label: String, app: ApplicationInfo): Boolean {
    if (app.packageName.startsWith("com.android.")) return true
    if (label.startsWith("com.")) return true
    if (label.lowercase() == app.packageName) return true
    return app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
}

fun isSystemByPackageName(ctx: Context, packageName: String): Boolean {
    val metadata = ctx.packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
    val label = ctx.packageManager.getApplicationLabel(metadata.applicationInfo).toString()

    if (metadata.packageName.startsWith("com.android.")) return true
    if (label.startsWith("com.")) return true
    if (label.lowercase() == metadata.packageName) return true
    return metadata.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
}

fun convertToBitmap(icon: Drawable): ImageBitmap {
    return icon.toBitmap().asImageBitmap()
}
