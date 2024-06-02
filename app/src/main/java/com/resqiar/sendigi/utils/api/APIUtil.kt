package com.resqiar.sendigi.utils.api

import android.content.Context
import android.util.Log
import com.resqiar.sendigi.ApplicationActivity
import com.resqiar.sendigi.constants.Constants
import com.resqiar.sendigi.dao.AppInfoDao
import com.resqiar.sendigi.entities.AppInfoEntity
import com.resqiar.sendigi.model.AppInfoMessage
import com.resqiar.sendigi.model.ApplicationMetadata
import com.resqiar.sendigi.model.DeviceInfo
import com.resqiar.sendigi.model.InstalledApp
import com.resqiar.sendigi.utils.bitmapToBase64
import com.resqiar.sendigi.utils.getApplicationMetadata
import com.resqiar.sendigi.utils.getDeviceInstalledApplication
import com.resqiar.sendigi.utils.getDeviceMetadata
import com.google.gson.Gson
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Consumer
import com.rabbitmq.client.Envelope
import com.rabbitmq.client.ShutdownSignalException
import com.resqiar.sendigi.model.RequestMessage
import com.resqiar.sendigi.model.WebRequestMessage
import com.resqiar.sendigi.utils.showNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getCurrentDeviceMetadata(context: Context) {
    val metadata = getDeviceMetadata(context)

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
    val metadata = getDeviceMetadata(context)

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

suspend fun sendRequestMessage(message: String, packageName: String, context: Context) {
    val metadata = getDeviceMetadata(context)

    // get token from shared pref
    val token =  context.getSharedPreferences(Constants.LOG_TOKEN_PREF, Context.MODE_PRIVATE)
        .getString(Constants.LOG_TOKEN_PREF, "") ?: ""

    // if there is no token, skip
    if (token.isEmpty()) return

    withContext(Dispatchers.IO) {
        val call = ApiClient.apiService.requestMessage(
            token = token,
            RequestMessage(
                message = message,
                packageName = packageName,
                deviceId = metadata.androidId
            ),
        )

        call.enqueue(object: Callback<StatusResponse> {
            override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                if (response.isSuccessful) {
                    val post = response.body()
                    Log.d("Sync Application", "Requesting message to remote server: ${metadata.androidId} -> ${post?.status}")
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

suspend fun saveState(
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

suspend fun updateState(
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

fun initListenMQ(ctx: Context) {
    // get device metadata
    val deviceMetadata = getDeviceMetadata(ctx)

    // get auth token
    val userId =  ctx.getSharedPreferences(Constants.LOG_USERID_PREF, Context.MODE_PRIVATE)
        .getString(Constants.LOG_USERID_PREF, "") ?: ""

    if (userId.isEmpty()) return

    val factory = ConnectionFactory().apply {
        username = Constants.RABBITMQ_USERNAME
        password = Constants.RABBITMQ_PASSWORD
        host = Constants.RABBITMQ_HOST
        virtualHost = ConnectionFactory.DEFAULT_VHOST
        port = Constants.RABBITMQ_PORT
    }

    val ch = factory.newConnection().createChannel()
    val q = ch.queueDeclare(
        "${userId}_${deviceMetadata.androidId}",
        false,
        false,
        false,
        null,
    )
    val request_msg_q = ch.queueDeclare(
        "message_${userId}_${deviceMetadata.androidId}",
        false,
        false,
        true,
        null,
    )

    ch.basicConsume(
        q.queue,
        true,
        object : Consumer {
            override fun handleDelivery(
                consumerTag: String?,
                envelope: Envelope?,
                properties: AMQP.BasicProperties?,
                body: ByteArray?
            ) {
                body?.let {
                    val message = Gson().fromJson(
                        it.decodeToString(),
                        AppInfoMessage::class.java
                    )

                    Log.d("[Queue] Syncing to local state: ", message.packageName)

                    // update local state to a new one
                    updateState(message)
                }
            }

            override fun handleShutdownSignal(consumerTag: String?, sig: ShutdownSignalException?) {
                sig?.let {
                    Log.d("RabbitMQ Shutdown Signal", consumerTag.toString())
                }
            }

            override fun handleConsumeOk(consumerTag: String?) {}
            override fun handleCancelOk(consumerTag: String?) {}
            override fun handleCancel(consumerTag: String?) {}
            override fun handleRecoverOk(consumerTag: String?) {}
        })

    ch.basicConsume(
        request_msg_q.queue,
        true,
        object : Consumer {
            override fun handleDelivery(
                consumerTag: String?,
                envelope: Envelope?,
                properties: AMQP.BasicProperties?,
                body: ByteArray?
            ) {
                body?.let {
                    val payload = Gson().fromJson(
                        it.decodeToString(),
                        WebRequestMessage::class.java
                    )

                    Log.d("[Queue] retrieving message from web: ", payload.message)
                    showNotification(ctx, payload.message)
                }
            }

            override fun handleShutdownSignal(consumerTag: String?, sig: ShutdownSignalException?) {
                sig?.let {
                    Log.d("RabbitMQ Shutdown Signal", consumerTag.toString())
                }
            }

            override fun handleConsumeOk(consumerTag: String?) {}
            override fun handleCancelOk(consumerTag: String?) {}
            override fun handleCancel(consumerTag: String?) {}
            override fun handleRecoverOk(consumerTag: String?) {}
        })
}

private fun updateState(message: AppInfoMessage) {
    CoroutineScope(Dispatchers.IO).launch {
        // get room data access object
        val infoDao = ApplicationActivity.getInstance().appInfoDao()

        // update local copy to a new one
        infoDao.serverUpdateAppInfo(
            packageName = message.packageName,
            lockStatus = message.lockStatus,
            dateLocked = message.dateLocked,
            timeStartLocked = message.timeStartLocked,
            timeEndLocked = message.timeEndLocked,
        )
    }
}