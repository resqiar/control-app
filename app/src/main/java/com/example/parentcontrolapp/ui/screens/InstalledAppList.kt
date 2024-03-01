package com.example.parentcontrolapp.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parentcontrolapp.utils.getInstalledApps

@Composable
fun InstalledAppsList(ctx: Context) {
    // remember will memoize the result from getInstalledApps
    val apps = remember {
        getInstalledApps(ctx)
    }

    LazyColumn(
        modifier = Modifier.padding(vertical = 18.dp)
    ) {
        items(apps.size) { idx ->
            AppList(metadata = apps[idx])
        }
    }
}