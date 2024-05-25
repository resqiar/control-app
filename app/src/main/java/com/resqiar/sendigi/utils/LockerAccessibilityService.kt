package com.resqiar.sendigi.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.resqiar.sendigi.ApplicationActivity
import com.resqiar.sendigi.LockScheduledActivity
import com.resqiar.sendigi.LockScreenActivity
import com.resqiar.sendigi.constants.Constants
import com.resqiar.sendigi.model.DeviceActivity
import com.resqiar.sendigi.utils.api.ApiClient
import com.resqiar.sendigi.utils.api.StatusResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

class LockerAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!event.packageName.isNullOrEmpty()) {
            val packageName = event.packageName.toString()
            val context = this
            val infoDao = ApplicationActivity.getInstance().appInfoDao()

            CoroutineScope(Dispatchers.IO).launch {
                // Get application info from local database
                val info = infoDao.getAppInfo(packageName)

                if (info != null) {
                    val current = Date()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val currentFormattedDate = dateFormat.format(current)

                    // if current opened app is locked by "Date Scheduler"
                    if (!info.lockDates.isNullOrEmpty()) {
                        var locked = false

                        // scan through dates delimiters and fire the lock activity when needed
                        val savedDates = info.lockDates.split(", ").map {
                            if (currentFormattedDate == it) locked = true
                            it
                        }

                        if (locked) {
                            // save log activity
                            syncDeviceActivity(context, info.name, info.packageName, "[Warning] Attempt to open locked application")

                            val intent = Intent(context, LockScheduledActivity::class.java)
                            intent.putExtra("PACKAGE_NAME", packageName)
                            intent.putExtra("DATES", savedDates.toTypedArray())
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    // if current opened app is locked by "Time Scheduler"
                    else if (!info.lockStartTime.isNullOrEmpty() && !info.lockEndTime.isNullOrEmpty()) {
                        // check if current time is in the range of start - end time
                        val locked = isWithinTimeRange(info.lockStartTime, info.lockEndTime)

                        if (locked) {
                            // save log activity
                            syncDeviceActivity(context, info.name, info.packageName, "[Warning] Attempt to open locked application")

                            val intent = Intent(context, LockScheduledActivity::class.java)
                            intent.putExtra("PACKAGE_NAME", packageName)
                            intent.putExtra("START_TIME", info.lockStartTime)
                            intent.putExtra("END_TIME", info.lockEndTime)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    // if current opened app is locked by "Specific"
                    else if (info.lockStatus) {
                        // save log activity
                        syncDeviceActivity(context, info.name, info.packageName, "[Warning] Attempt to open locked application")

                        val intent = Intent(context, LockScreenActivity::class.java)
                        intent.putExtra("PACKAGE_NAME", packageName)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }

                    else {
                        // Save neutral log
                        syncDeviceActivity(context, info.name, info.packageName, "[Info] Opening application")
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        val info = AccessibilityServiceInfo()

        info.apply {
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
            notificationTimeout = 100
        }

        this.serviceInfo = info
    }

    private fun syncDeviceActivity(context: Context, name: String, packageName: String, desc: String) {
            // skip logging parental app
            if (packageName == context.packageName) return

            val metadata = getDeviceMetadata(context)

            val token =  context.getSharedPreferences(Constants.LOG_TOKEN_PREF, Context.MODE_PRIVATE)
                .getString(Constants.LOG_TOKEN_PREF, "") ?: ""

            // if there is no token, skip
            if (token.isEmpty()) return

            val call = ApiClient.apiService.syncDeviceActivity(
                token = token,
                DeviceActivity(
                    deviceId = metadata.androidId,
                    name = name,
                    packageName = packageName,
                    description = desc,
                )
            )

            call.enqueue(object: Callback<StatusResponse> {
                override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                    if (response.isSuccessful) {
                        val post = response.body()
                        Log.d("Sync Device Activity", "Syncing to remote server for: ${metadata.androidId} -> ${post?.status}")
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

fun isWithinTimeRange(startTime: String, endTime: String): Boolean {
    val currentTime = LocalTime.now()
    val currentTimeMinutes = currentTime.hour * 60 + currentTime.minute

    val startTimeParts = startTime.split(":").map { it.toInt() }
    val endTimeParts = endTime.split(":").map { it.toInt() }

    val startHour = startTimeParts[0]
    val startMinute = startTimeParts[1]

    val endHour = endTimeParts[0]
    val endMinute = endTimeParts[1]

    val startTimeMinutes = startHour * 60 + startMinute
    val endTimeMinutes = endHour * 60 + endMinute

    return if (startTimeMinutes <= endTimeMinutes) {
        currentTimeMinutes in startTimeMinutes until endTimeMinutes
    } else {
        currentTimeMinutes >= startTimeMinutes || currentTimeMinutes < endTimeMinutes
    }
}