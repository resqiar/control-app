package com.resqiar.sendigi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.resqiar.sendigi.ui.screens.LockScheduledScreen

class LockScheduledActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receivedPkgName = intent.getStringExtra("PACKAGE_NAME")
        val receivedDates: Array<String>? = intent.getStringArrayExtra("DATES")
        val receivedStartTime = intent.getStringExtra("START_TIME")
        val receivedEndTime = intent.getStringExtra("END_TIME")

        setContent {
            if (receivedPkgName != null) {
                LockScheduledScreen(receivedPkgName, receivedDates, receivedStartTime, receivedEndTime)
            }
        }
    }
}