package com.example.parentcontrolapp.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.accessibility.AccessibilityEvent
import com.example.parentcontrolapp.LockScreenActivity
import com.example.parentcontrolapp.constants.Constants

class LockerAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString()

        // Get the shared preferences using the application context
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences(
            Constants.LOCKED_APPS_PREF,
            Context.MODE_PRIVATE
        )

        // Retrieve the value from shared preferences
        val isLocked = sharedPreferences.getBoolean(packageName, false)

        if (isLocked) {
            val intent = Intent(this, LockScreenActivity::class.java)
            intent.putExtra("PACKAGE_NAME", packageName)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
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