package com.example.parentcontrolapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.parentcontrolapp.ui.screens.InstalledAppsList
import com.example.parentcontrolapp.ui.theme.ParentControlAppTheme
import com.example.parentcontrolapp.utils.getInstalledApps
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.State

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // splash screen init
        installSplashScreen()

        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            val apps = getInstalledApps(applicationContext)

            setContent {
                ParentControlAppTheme {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Application Screen Time",
                                    fontWeight = FontWeight.Medium
                                )
                            },
                        )
                        InstalledAppsList(apps)
                    }
                }
            }
        }
    }
}