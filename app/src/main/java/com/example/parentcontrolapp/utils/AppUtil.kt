package com.example.parentcontrolapp.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

fun isSystemByPackageName(ctx: Context, packageName: String, appInfo: ApplicationInfo): Boolean {
    val metadata = ctx.packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
    val label = ctx.packageManager.getApplicationLabel(metadata.applicationInfo).toString()

    val systemAppPackage = arrayOf(
        "android",
        "com.android",
        "com.google.android.ext",
        "com.google.android.networkstack",
        "com.google.android.packageinstaller",
        "com.google.android.onetimeinitializer",
        "com.google.android.configupdater",
        "com.google.android.providers",
        "com.google.android.providers",
        "com.google.android.hotspot2",
        "com.google.android.adservices",
        "com.google.android.bluetooth",
        "com.google.android.as.oss",
        "com.google.android.devicelockcontroller",
        "com.google.android.permissioncontroller",
        "com.google.android.captiveportallogin",
        "com.google.android.modulemetadata",
        "com.google.android.webview",
        "com.google.android.rkpdapp",
        "com.google.android.gsf",
        "com.google.android.gms",
        "com.google.android.printservice",
        "com.google.mainline",
        "com.google.android.system",
        "com.google.android.uwb",
        "com.google.android.wifi",
        "com.google.android.odad",
        "com.google.android.cellbroadcastservice",
        "com.google.android.safetycenter",
        "com.google.android.resources",
    )

    for (pkg in systemAppPackage) {
        if (metadata.packageName.startsWith(pkg)) return true
    }

    if (label.startsWith("com.")) return true
    if (label.lowercase() == metadata.packageName) return true
    return appInfo.publicSourceDir.startsWith("/system")
}

fun convertToBitmap(icon: Drawable): ImageBitmap {
    return icon.toBitmap().asImageBitmap()
}

fun convertToHours(minutes: Long): Pair<Long, Long> {
    val hour = minutes / 60
    val min = minutes % 60
    // wow even kotlin has some kind of tuples
    return Pair(hour, min)
}
