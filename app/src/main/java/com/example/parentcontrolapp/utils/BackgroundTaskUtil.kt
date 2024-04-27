package com.example.parentcontrolapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.BatteryManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import com.example.parentcontrolapp.constants.Constants
import com.example.parentcontrolapp.model.DeviceInfo
import com.example.parentcontrolapp.utils.api.ApiClient
import com.example.parentcontrolapp.utils.api.StatusResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object BackgroundTaskUtil {
    fun startBackgroundTask(context: Context) {
        val handler = Handler(Looper.getMainLooper())
        val delayMillis: Long = 60 * 1000 // 1 minute interval

        val runnable = object : Runnable {
            override fun run() {
                getAndSendMetadata(context)
                handler.postDelayed(this, delayMillis)
            }
        }

        handler.post(runnable)
    }

    @SuppressLint("HardwareIds")
    fun getAndSendMetadata(context: Context) {
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

        val token =  context.getSharedPreferences(Constants.LOG_TOKEN_PREF, Context.MODE_PRIVATE)
            .getString(Constants.LOG_TOKEN_PREF, "") ?: ""

        val call = ApiClient.apiService.syncDevice(
            token = token,
            DeviceInfo(
                androidId = androidID,
                deviceName = deviceName,
                deviceBrand = deviceBrand,
                apiLevel = apiLevel,
                androidVersion = androidVersion,
                manufacturer = manufacturer,
                productName = productName,
                batteryLevel = batteryLevel,
                isCharging = isCharging,
            )
        )

        call.enqueue(object: Callback<StatusResponse> {
            override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                if (response.isSuccessful) {
                    val post = response.body()
                    Log.d("RESPONSE", post?.status.toString())
                } else {
                    Log.d("HTTP NOT OK", response.toString())
                }
            }

            override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                Log.d("HTTP FAILURE", t.message.toString())
            }
        })
    }
}