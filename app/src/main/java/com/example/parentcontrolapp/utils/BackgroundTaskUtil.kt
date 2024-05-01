package com.example.parentcontrolapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.example.parentcontrolapp.constants.Constants
import com.example.parentcontrolapp.model.AppInfo
import com.example.parentcontrolapp.model.ApplicationMetadata
import com.example.parentcontrolapp.model.DeviceInfo
import com.example.parentcontrolapp.model.InstalledApp
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
                getCurrentDeviceMetadata(context)

                CoroutineScope(Dispatchers.Default).launch {
                    sendApplicationDataWithDeviceData(context)
                }

                handler.postDelayed(this, delayMillis)
            }
        }

        handler.post(runnable)
    }

    @SuppressLint("HardwareIds")
    fun getCurrentDeviceMetadata(context: Context) {
        val metadata = GetDeviceMetadata(context)

        val token =  context.getSharedPreferences(Constants.LOG_TOKEN_PREF, Context.MODE_PRIVATE)
            .getString(Constants.LOG_TOKEN_PREF, "") ?: ""

        // if there is no token, skip
        if (token.isEmpty()) return

        val call = ApiClient.apiService.syncDevice(
            token = token,
            DeviceInfo(
                androidId = metadata.androidId,
                deviceName = metadata.deviceName,
                deviceBrand = metadata.deviceBrand,
                apiLevel = metadata.apiLevel,
                androidVersion = metadata.androidVersion,
                manufacturer = metadata.manufacturer,
                productName = metadata.productName,
                batteryLevel = metadata.batteryLevel,
                isCharging = metadata.isCharging,
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

    suspend fun sendApplicationDataWithDeviceData(context: Context) {
        val metadata = GetDeviceMetadata(context)

        val token =  context.getSharedPreferences(Constants.LOG_TOKEN_PREF, Context.MODE_PRIVATE)
            .getString(Constants.LOG_TOKEN_PREF, "") ?: ""

        // if there is no token, skip
        if (token.isEmpty()) return

        withContext(Dispatchers.Default) {
            val updatedApps = getDeviceInstalledApplication(context)

            for (app in updatedApps) {
                val appMetadata = getApplicationMetadata(context, app, metadata)

                val call = ApiClient.apiService.syncApp(
                    token = token,
                    appMetadata.info
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

    private fun getApplicationMetadata(
        context: Context,
        app: InstalledApp,
        metadata: DeviceInfo
    ): ApplicationMetadata {
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
            lockStatus = status,
            androidId = metadata.androidId,
        )

        return ApplicationMetadata(
            status,
            icon,
            appInfo
        )
    }

    private fun bitmapToBase64(raw: ImageBitmap?): String {
        if (raw == null) return ""

        val bitmap = raw.asAndroidBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
    }
}