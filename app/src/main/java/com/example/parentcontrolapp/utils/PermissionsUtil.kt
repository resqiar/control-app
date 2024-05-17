package com.example.parentcontrolapp.utils

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager

fun getUsageStatsPermission(ctx: Context): Boolean {
    val appOps = ctx.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), ctx.applicationContext.packageName)
    return mode == AppOpsManager.MODE_ALLOWED
}

fun askUsageStatsPermission(ctx: Context) {
    ctx.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
}

fun checkAccessibility(ctx: Context): Boolean {
    val accessibilityManager =
        ctx.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val accessibilityService = accessibilityManager.getEnabledAccessibilityServiceList(
        AccessibilityServiceInfo.FEEDBACK_ALL_MASK
    )
    return accessibilityService.any {
        it.resolveInfo.serviceInfo.packageName == ctx.packageName && it.resolveInfo.serviceInfo.name == "${ctx.packageName}.utils.LockerAccessibilityService"
    }
}

fun askAccessibilityPermission(ctx: Context) {
    ctx.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
}