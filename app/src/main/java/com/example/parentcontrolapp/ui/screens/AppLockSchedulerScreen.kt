package com.example.parentcontrolapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parentcontrolapp.ui.theme.AppTheme
import com.example.parentcontrolapp.viewModel.MainViewModel


@Composable
fun AppLockSchedulerScreen(navController: NavController) {
    val viewModel: MainViewModel = viewModel()
    AppTheme {
        val apps by viewModel.installedApps.observeAsState(
            ArrayList(emptyList())
        )
        Box(modifier = Modifier.fillMaxSize()) {
            val appBarHeight = 64.dp
            Surface(modifier = Modifier.padding(top = appBarHeight)) {
                Column {
                    InstalledAppsList(apps = apps, onItemClick = { app ->
                        println("Clicked on app: ${app.name}")
                        navController.navigate("scheduling/${app.packageName}/${app.name}")
                    })
                }

            }
        }
    }

}