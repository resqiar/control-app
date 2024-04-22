package com.example.parentcontrolapp.ui.screens

import AppLockViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parentcontrolapp.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun AppLockScreen() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "App Lock Scheduler", fontSize = 36.sp,)
            }
            InstalledAppsScreen()
        }
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstalledAppsScreen(
    appLockViewModel: AppLockViewModel = viewModel()
) {
    val context = LocalContext.current
    val installedAppsState = remember { mutableStateOf<List<String>>(emptyList()) }
    val changesApplied = remember { mutableStateOf(false) }

    // Retrieve installed apps when the screen is first displayed
    DisposableEffect(key1 = context) {
        val installedApps = appLockViewModel.getAllInstalledApps(context)
        installedAppsState.value = installedApps
        onDispose {}
    }

    Scaffold {
        Column {
            AppList(installedAppsState.value, appLockViewModel)
            // Show feedback to the user if changes have been applied
            if (changesApplied.value) {
                Text(
                    text = "Changes Applied",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            // Apply Changes button
            Button(
                onClick = {
                    // Get the list of locked apps
                    val lockedApps = appLockViewModel.getAllLockedApps(context)
                    // Apply changes
                    installedAppsState.value.forEach { packageName ->
                        if (appLockViewModel.isAppLocked(context, packageName) != packageName in lockedApps) {
                            if (packageName in lockedApps) {
                                appLockViewModel.lockApp(context, packageName)
                            } else {
                                appLockViewModel.unlockApp(context, packageName)
                            }
                        }
                    }
                    // Set changes applied state
                    changesApplied.value = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Apply Changes")
            }
        }
    }
}

@Composable
fun AppList(
    installedApps: List<String>,
    appLockViewModel: AppLockViewModel
) {
    LazyColumn {
        items(installedApps) { packageName ->
            AppItem(packageName, appLockViewModel)
        }
    }
}

@Composable
fun AppItem(
    packageName: String,
    appLockViewModel: AppLockViewModel
) {
    val context = LocalContext.current
    var isAppLocked by remember { mutableStateOf(appLockViewModel.isAppLocked(context, packageName)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Toggle the checkbox state and update the UI
                isAppLocked = !isAppLocked
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = packageName,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isAppLocked,
                onCheckedChange = { isChecked ->
                    isAppLocked = isChecked
                    if (isChecked) {
                        appLockViewModel.lockApp(context, packageName)
                    } else {
                        appLockViewModel.unlockApp(context, packageName)
                    }
                }
            )
        }
    }
}