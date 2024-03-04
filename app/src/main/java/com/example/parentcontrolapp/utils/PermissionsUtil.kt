package com.example.parentcontrolapp.utils

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
fun getUsageStatsPermission(ctx: Context): Boolean {
    val appOps = ctx.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), ctx.applicationContext.packageName)
    return mode == AppOpsManager.MODE_ALLOWED
}

fun askUsageStatsPermission(ctx: Context) {
    ctx.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
}