package com.example.parentcontrolapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentcontrolapp.ui.screens.InstalledAppsList
import com.example.parentcontrolapp.ui.theme.ParentControlAppTheme
import com.example.parentcontrolapp.utils.getInstalledApps
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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