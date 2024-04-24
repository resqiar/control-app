package com.example.parentcontrolapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.parentcontrolapp.ui.theme.AppTheme
import com.example.parentcontrolapp.utils.checkAccessibility
import com.example.parentcontrolapp.viewModel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        // check permissions for accessibility
        if (!checkAccessibility(this)) {
            val intent = Intent(this, AccessibilityPermissionActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        setContent {
            val apps by viewModel.installedApps.observeAsState(ArrayList(emptyList()))

            AppTheme {
                NavDrawer()
            }
        }
    }
}