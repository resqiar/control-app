package com.example.parentcontrolapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.parentcontrolapp.ui.theme.AppTheme
import com.example.parentcontrolapp.utils.checkAccessibility


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
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
            AppTheme {
                NavDrawer()
            }
        }
    }
}