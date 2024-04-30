package com.example.parentcontrolapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parentcontrolapp.ui.theme.AppTheme
import com.example.parentcontrolapp.utils.BackgroundTaskUtil
import com.example.parentcontrolapp.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        // trigger background task
        BackgroundTaskUtil.startBackgroundTask(this)

        setContent {
            AppTheme {
                val viewModel: AuthViewModel = viewModel()

                if (viewModel.isLoggedIn.value == true) {
                    NavDrawer()
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}