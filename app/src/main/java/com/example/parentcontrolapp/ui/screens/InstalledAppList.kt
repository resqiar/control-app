package com.example.parentcontrolapp.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parentcontrolapp.model.InstalledApp
import com.example.parentcontrolapp.utils.getInstalledApps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun InstalledAppsList(apps: ArrayList<InstalledApp>) {
    LazyColumn(
        modifier = Modifier.padding(vertical = 0.dp)
    ) {
        items(apps.size) { idx ->
            AppList(metadata = apps[idx])
        }
    }
}