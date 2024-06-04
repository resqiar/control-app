package com.resqiar.sendigi.model

data class AppInfo (
    val name: String,
    val packageName: String,
    val icon: String,
    val timeUsage: Long,
    val lockStatus: Boolean,
    val deviceId: String,
    val dateLocked: String,
    val timeStartLocked: String,
    val timeEndLocked: String,
    val recurring: String,
)

data class AppInfoMessage (
    val name: String,
    val packageName: String,
    val timeUsage: Long,
    val lockStatus: Boolean,
    val dateLocked: String,
    val timeStartLocked: String,
    val timeEndLocked: String,
    val recurring: String,
)
