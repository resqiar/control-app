package com.resqiar.sendigi.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.resqiar.sendigi.dao.AppInfoDao
import com.resqiar.sendigi.model.AppInfo
import com.resqiar.sendigi.model.AppScheduledData
import com.resqiar.sendigi.model.ApplicationMetadata
import com.resqiar.sendigi.model.DeviceInfo
import com.resqiar.sendigi.model.InstalledApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

fun excludeSystemApplication(ctx: Context, packageName: String): Boolean {
    // if package does not have launch intent, then that is a system app
    ctx.packageManager.getLaunchIntentForPackage(packageName) ?: return true

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
    return label.lowercase() == metadata.packageName
}

fun convertToBitmap(icon: Drawable): ImageBitmap {
    return icon.toBitmap().asImageBitmap()
}

fun bitmapToBase64(raw: ImageBitmap?): String {
    if (raw == null) return ""

    val bitmap = raw.asAndroidBitmap()
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
}

fun convertToHours(minutes: Long): Pair<Long, Long> {
    val hour = minutes / 60
    val min = minutes % 60
    // wow even kotlin has some kind of tuples
    return Pair(hour, min)
}

suspend fun getApplicationMetadata(
    app: InstalledApp,
    metadata: DeviceInfo,
    infoDao: AppInfoDao
): ApplicationMetadata {
    // get lock status for current app in local database
    return withContext(Dispatchers.IO) {
        val status = infoDao.getLockStatus(packageName = app.packageName)
        val data = infoDao.getAppInfo(app.packageName)

        // convert bitmap icon to base64
        val icon = bitmapToBase64(app.icon)
        val appInfo = AppInfo(
            name = app.name,
            packageName = app.packageName,
            icon = icon,
            timeUsage = app.rawTime,
            lockStatus = status,
            androidId = metadata.androidId,
            dateLocked = data?.lockDates ?: "",
            timeStartLocked = data?.lockStartTime ?: "",
            timeEndLocked = data?.lockEndTime ?: "",
        )

        ApplicationMetadata(
            status,
            icon,
            appInfo,
            AppScheduledData(
                lockDates = data?.lockDates,
                lockStartTime = data?.lockStartTime,
                lockEndTime = data?.lockEndTime
            ),
        )
    }
}
