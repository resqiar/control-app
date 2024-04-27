package com.example.parentcontrolapp.model

data class DeviceInfo(
    val androidId: String,
    val deviceName: String,
    val deviceBrand: String,
    val apiLevel: Int,
    val androidVersion: String,
    val manufacturer: String,
    val productName: String,
    val batteryLevel: Int,
    val isCharging: Boolean,
)