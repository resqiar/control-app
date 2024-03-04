package com.example.parentcontrolapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.parentcontrolapp.ui.screens.InstalledAppsList
import com.example.parentcontrolapp.ui.theme.ParentControlAppTheme
import com.example.parentcontrolapp.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // splash screen init
        installSplashScreen()

        setContent {
            // @Reactivity
            // observe changed value from the view model
            val apps by viewModel.installedApps.observeAsState(
                ArrayList(emptyList())
            )

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