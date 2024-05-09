package com.example.parentcontrolapp.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.example.parentcontrolapp.ApplicationActivity
import com.example.parentcontrolapp.LockScheduledActivity
import com.example.parentcontrolapp.LockScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LockerAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && !event.packageName.isNullOrEmpty()) {
            val packageName = event.packageName.toString()
            val context = this
            val infoDao = ApplicationActivity.getInstance().appInfoDao()

            CoroutineScope(Dispatchers.IO).launch {
                // Get application info from local database
                val info = infoDao.getAppInfo(packageName)

                if (info != null) {
                    val current = Date()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val currentFormattedDate = dateFormat.format(current)
                    val currentFormattedTime = timeFormat.format(current)

                    // if current opened app is locked by "Date Scheduler"
                    if (!info.lockDates.isNullOrEmpty()) {
                        var locked = false

                        // scan through dates delimiters and fire the lock activity when needed
                        val savedDates = info.lockDates.split(", ").map {
                            if (currentFormattedDate == it) locked = true
                            it
                        }

                        if (locked) {
                            val intent = Intent(context, LockScheduledActivity::class.java)
                            intent.putExtra("PACKAGE_NAME", packageName)
                            intent.putExtra("DATES", savedDates.toTypedArray())
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    // if current opened app is locked by "Time Scheduler"
                    else if (!info.lockStartTime.isNullOrEmpty() && !info.lockEndTime.isNullOrEmpty()) {
                        // change current time to calendar instance
                        val currentCalendar = Calendar.getInstance()
                        currentCalendar.time = timeFormat.parse(currentFormattedTime)!!

                        // change lock start time to calendar instance
                        val startCalendar = Calendar.getInstance()
                        startCalendar.time = timeFormat.parse(info.lockStartTime)!!

                        // change lock end time to calendar instance
                        val endCalendar = Calendar.getInstance()
                        endCalendar.time = timeFormat.parse(info.lockEndTime)!!

                        // convert all calendars to time in milliseconds
                        val currentTimeMillis = currentCalendar.timeInMillis
                        val startTimeMillis = startCalendar.timeInMillis
                        val endTimeMillis = endCalendar.timeInMillis

                        // check if current time is in the range of start - end time
                        val locked = currentTimeMillis in startTimeMillis..<endTimeMillis

                        if (locked) {
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
                        val intent = Intent(context, LockScreenActivity::class.java)
                        intent.putExtra("PACKAGE_NAME", packageName)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
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
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
            notificationTimeout = 100
        }

        this.serviceInfo = info
    }
}