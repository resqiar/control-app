package com.example.parentcontrolapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.parentcontrolapp.ui.screens.LockScreen

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