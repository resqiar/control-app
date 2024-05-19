package com.resqiar.sendigi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.resqiar.sendigi.ui.screens.LockScreen

class LockScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receivedPkgName = intent.getStringExtra("PACKAGE_NAME")

        setContent {
            if (receivedPkgName != null) {
                LockScreen(receivedPkgName)
            }
        }
    }
}