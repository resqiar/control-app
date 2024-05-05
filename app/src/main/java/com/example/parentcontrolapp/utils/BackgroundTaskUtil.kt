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
import com.example.parentcontrolapp.ApplicationActivity
import com.example.parentcontrolapp.constants.Constants
import com.example.parentcontrolapp.dao.AppInfoDao
import com.example.parentcontrolapp.entities.AppInfoEntity
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
    private var isBackgroundRunning = false

    fun startBackgroundTask(context: Context) {
        if (!isBackgroundRunning) {
            val handler = Handler(Looper.getMainLooper())
            val delayMillis: Long = 60 * 1000 // 1 minute interval

            val runnable = object : Runnable {
                override fun run() {
                    CoroutineScope(Dispatchers.IO).launch {
                        getCurrentDeviceMetadata(context)
                        sendApplicationDataWithDeviceData(context)
                    }

                    if (isBackgroundRunning) {
                        handler.postDelayed(this, delayMillis)
                    }
                }
            }

            handler.post(runnable)
        }
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
                    Log.d("Sync Device", "Syncing to remote server for: ${metadata.androidId} -> ${post?.status}")
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

        // get token from shared pref
        val token =  context.getSharedPreferences(Constants.LOG_TOKEN_PREF, Context.MODE_PRIVATE)
            .getString(Constants.LOG_TOKEN_PREF, "") ?: ""

        // if there is no token, skip
        if (token.isEmpty()) return

        withContext(Dispatchers.IO) {
            val updatedApps = getDeviceInstalledApplication(context)

            for (app in updatedApps) {

                // before sending to remote server, save the state to local database
                val infoDao = ApplicationActivity.getInstance().appInfoDao()
                val appMetadata = getApplicationMetadata(app, metadata, infoDao)
                val exist = infoDao.getAppInfo(appMetadata.info.packageName)

                // if the app package name is not yet created in the database
                if (exist == null) {
                    saveState(app, appMetadata, infoDao)
                } else { // otherwise, just update as usual
                    updateState(app, appMetadata, infoDao)
                }

                val call = ApiClient.apiService.syncApp(
                    token = token,
                    appMetadata.info
                )

                call.enqueue(object: Callback<StatusResponse> {
                    override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                        if (response.isSuccessful) {
                            val post = response.body()
                            Log.d("Sync Application", "Syncing to remote server for: ${appMetadata.info.name} -> ${post?.status}")
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

    private suspend fun saveState(
        app: InstalledApp,
        metadata: ApplicationMetadata,
        infoDao: AppInfoDao
    ) {
        withContext(Dispatchers.IO) {
            infoDao.createAppInfo(
                AppInfoEntity(
                    packageName = metadata.info.packageName,
                    icon = bitmapToBase64(app.icon),
                    timeUsage = app.rawTime,
                    lockStatus = metadata.info.lockStatus,
                    name = metadata.info.name
                )
            )
        }
    }

    private suspend fun updateState(
        app: InstalledApp,
        metadata: ApplicationMetadata,
        infoDao: AppInfoDao
    ) {
        withContext(Dispatchers.IO) {
            infoDao.updateAppInfo(
                AppInfoEntity(
                    packageName = metadata.info.packageName,
                    icon = bitmapToBase64(app.icon),
                    timeUsage = app.rawTime,
                    lockStatus = metadata.info.lockStatus,
                    name = metadata.info.name
                )
            )
        }
    }

    private suspend fun getApplicationMetadata(
        app: InstalledApp,
        metadata: DeviceInfo,
        infoDao: AppInfoDao
    ): ApplicationMetadata {
        // get lock status for current app in local database
        return withContext(Dispatchers.IO) {
            val status = infoDao.getLockStatus(packageName = app.packageName)

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

            ApplicationMetadata(
                status,
                icon,
                appInfo
            )
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