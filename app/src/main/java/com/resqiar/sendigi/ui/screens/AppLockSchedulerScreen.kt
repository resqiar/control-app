package com.resqiar.sendigi.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.resqiar.sendigi.AccessibilityPermissionActivity
import com.resqiar.sendigi.ui.theme.AppTheme
import com.resqiar.sendigi.utils.checkAccessibility
import com.resqiar.sendigi.viewModel.MainViewModel


@Composable
fun AppLockSchedulerScreen(navController: NavController) {
    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current

    // check permissions for accessibility
    if (!checkAccessibility(context)) {
        val intent = Intent(context, AccessibilityPermissionActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

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