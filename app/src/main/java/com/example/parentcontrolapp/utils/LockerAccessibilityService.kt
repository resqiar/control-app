package com.example.parentcontrolapp.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.example.parentcontrolapp.ApplicationActivity
import com.example.parentcontrolapp.LockScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LockerAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString()
        val context = this
        val infoDao = ApplicationActivity.getInstance().appInfoDao()

        CoroutineScope(Dispatchers.IO).launch {
            // Get application lock status from local database
            val locked = infoDao.getLockStatus(packageName.toString())

            if (locked) {
                val intent = Intent(context, LockScreenActivity::class.java)
                intent.putExtra("PACKAGE_NAME", packageName)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
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