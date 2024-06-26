package com.resqiar.sendigi.ui.screens
import androidx.compose.runtime.Composable
import com.resqiar.sendigi.ui.theme.AppTheme
import com.resqiar.sendigi.viewModel.MainViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel







@Composable
fun AppUsageScreen() {
    val viewModel: MainViewModel = viewModel()
    AppTheme {
        val apps by viewModel.installedApps.observeAsState(
            ArrayList(emptyList())
        )
        val appBarHeight = 64.dp
        Surface(modifier = Modifier.padding(top = appBarHeight)) {
            InstalledAppsUsageList(apps = apps)
        }

    }

}