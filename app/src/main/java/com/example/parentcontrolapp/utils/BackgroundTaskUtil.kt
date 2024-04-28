package com.example.parentcontrolapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.BatteryManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.example.parentcontrolapp.constants.Constants
import com.example.parentcontrolapp.model.AppInfo
import com.example.parentcontrolapp.model.DeviceInfo
import com.example.parentcontrolapp.utils.api.ApiClient
import com.example.parentcontrolapp.utils.api.StatusResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

object BackgroundTaskUtil {
    fun startBackgroundTask(context: Context) {
        val handler = Handler(Looper.getMainLooper())
        val delayMillis: Long = 60 * 1000 // 1 minute interval

        val runnable = object : Runnable {
            override fun run() {
                getAndSendMetadata(context)
                CoroutineScope(Dispatchers.Default).launch {
                    getAndSendAppMetadata(context)
                }
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

        // if there is no token, skip
        if (token.isEmpty()) return

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
                    Log.d("DEVICE INFO RESPONSE", post?.status.toString())
                } else {
                    Log.d("HTTP NOT OK", response.toString())
                }
            }

            override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                Log.d("HTTP FAILURE", t.message.toString())
            }
        })
    }

    suspend fun getAndSendAppMetadata(context: Context) {
        val token =  context.getSharedPreferences(Constants.LOG_TOKEN_PREF, Context.MODE_PRIVATE)
            .getString(Constants.LOG_TOKEN_PREF, "") ?: ""

        // if there is no token, skip
        if (token.isEmpty()) return

        withContext(Dispatchers.Default) {
            val updatedApps = getInstalledApps(context)
            Log.d("INSTALLED APP", updatedApps.toString())

            for (app in updatedApps) {
                // get lock status for current app
                val status = context.getSharedPreferences(
                    Constants.LOCKED_APPS_PREF, Context.MODE_PRIVATE
                ).getBoolean(app.packageName, false)

                // convert bitmap icon to base64
                val icon = bitmapToBase64(app.icon)
                val appInfo = AppInfo(
                    name = app.name,
                    packageName = app.packageName,
                    icon = icon,
                    timeUsage = app.rawTime,
                    lockStatus = status
                )

                val call = ApiClient.apiService.syncApp(
                    token = token,
                    appInfo
                )

                call.enqueue(object: Callback<StatusResponse> {
                    override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                        if (response.isSuccessful) {
                            val post = response.body()
                            Log.d("APP INFO RESPONSE", post?.status.toString())
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
    }

    private fun bitmapToBase64(raw: ImageBitmap?): String {
        if (raw == null) return ""

        val bitmap = raw.asAndroidBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
    }
}