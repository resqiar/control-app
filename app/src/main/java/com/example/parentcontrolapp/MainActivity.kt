package com.example.parentcontrolapp

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.parentcontrolapp.ui.theme.AppTheme
import com.example.parentcontrolapp.viewModel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        val serviceIntent = Intent(this, AppLockBackgroundService::class.java)
        startService(serviceIntent)

        // Check if the service is running
        val isServiceRunning = isServiceRunning(this, AppLockBackgroundService::class.java)
        Log.d("MainActivity", "Is service running: $isServiceRunning")

        setContent {
            val apps by viewModel.installedApps.observeAsState(ArrayList(emptyList()))

            AppTheme {
                NavDrawer()
            }
        }
    }

    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val runningServices = manager?.getRunningServices(Int.MAX_VALUE)
        if (runningServices != null) {
            for (serviceInfo in runningServices) {
                if (serviceInfo.service.className == serviceClass.name) {
                    return true
                }
            }
        }
        return false
    }
}



