package com.resqiar.sendigi.utils

import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.os.Process
import android.text.TextUtils

fun getUsageStatsPermission(ctx: Context): Boolean {
    val appOps = ctx.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), ctx.applicationContext.packageName)
    return mode == AppOpsManager.MODE_ALLOWED
}

fun askUsageStatsPermission(ctx: Context) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    ctx.startActivity(intent)
}

fun checkAccessibility(ctx: Context): Boolean {
    val accessibilityManager =
        ctx.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val accessibilityService = accessibilityManager.getEnabledAccessibilityServiceList(
        AccessibilityServiceInfo.FEEDBACK_ALL_MASK
    )
    return accessibilityService.any {
        val result = it.resolveInfo.serviceInfo.packageName == ctx.packageName && it.resolveInfo.serviceInfo.name == "${ctx.packageName}.utils.LockerAccessibilityService"
        result
    }
}

fun askAccessibilityPermission(ctx: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    ctx.startActivity(intent)
}

object XiaomiUtilities {
    const val OP_BACKGROUND_START_ACTIVITY = 10021

    fun isMIUI(): Boolean {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"))
    }

    @SuppressLint("PrivateApi")
    private fun getSystemProperty(key: String): String? {
        return try {
            val props = Class.forName("android.os.SystemProperties")
            val method = props.getMethod("get", String::class.java)
            method.invoke(null, key) as String
        } catch (e: Exception) {
            null
        }
    }

    fun isCustomPermissionGranted(context: Context, permission: Int): Boolean {
        return try {
            val mgr = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val method = AppOpsManager::class.java.getMethod(
                "checkOpNoThrow",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                String::class.java
            )
            val result = method.invoke(mgr, permission, Process.myUid(), context.packageName) as Int
            result == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            true
        }
    }

    fun getPermissionManagerIntent(context: Context): Intent {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.putExtra("extra_package_uid", Process.myUid())
        intent.putExtra("extra_pkgname", context.packageName)
        intent.putExtra("extra_package_name", context.packageName)
        return intent
    }
}