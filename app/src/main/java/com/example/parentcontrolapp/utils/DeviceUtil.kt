package com.example.parentcontrolapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import com.example.parentcontrolapp.model.DeviceInfo

@SuppressLint("HardwareIds")
fun GetDeviceMetadata(context: Context): DeviceInfo {
    val androidID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    val deviceName: String = Build.MODEL
    val deviceBrand: String = Build.BRAND
    val apiLevel: Int = Build.VERSION.SDK_INT
    val androidVersion: String = Build.VERSION.RELEASE
    val manufacturer: String = Build.MANUFACTURER
    val productName: String = Build.PRODUCT
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    val batteryLevel: Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    val isCharging: Boolean = batteryManager.isCharging

    return DeviceInfo(
        androidID,
        deviceName,
        deviceBrand,
        apiLevel,
        androidVersion,
        manufacturer,
        productName,
        batteryLevel,
        isCharging
    )
}
