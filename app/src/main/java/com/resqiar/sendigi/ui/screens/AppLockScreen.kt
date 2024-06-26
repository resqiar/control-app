package com.resqiar.sendigi.ui.screens

import com.resqiar.sendigi.viewModel.AppLockViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.resqiar.sendigi.PopUpWindowPermissionActivity
import com.resqiar.sendigi.model.InstalledApp
import com.resqiar.sendigi.ui.theme.AppTheme
import com.resqiar.sendigi.utils.XiaomiUtilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AppLockScreen() {
    val context = LocalContext.current

    // check additional permission for Xiaomi device
    if (XiaomiUtilities.isMIUI()) {
        if (!XiaomiUtilities.isCustomPermissionGranted(context, XiaomiUtilities.OP_BACKGROUND_START_ACTIVITY)) {
            val intent = Intent(context, PopUpWindowPermissionActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "App Lock Scheduler", fontSize = 36.sp)
            }
            InstalledAppsScreen()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun InstalledAppsScreen(
    viewModel: AppLockViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val apps by viewModel.installedApps.observeAsState(
        ArrayList(emptyList())
    )

    Scaffold {
        Column {
            AppList(apps, viewModel)

            Button(
                onClick = {
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            // Get the list of locked apps
                            val lockedApps = viewModel.getAllLockedApps()

                            // Apply changes
                            apps.forEach { app ->
                                if (viewModel.getLockStatus(app.packageName) != app.packageName in lockedApps) {
                                    if (app.packageName in lockedApps) {
                                        viewModel.lockApplication(ctx, app.packageName, true)
                                    } else {
                                        viewModel.lockApplication(ctx, app.packageName, false)
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {}
        }
    }
}

@Composable
fun AppList(
    installedApps: List<InstalledApp>,
    viewModel: AppLockViewModel
) {
    LazyColumn(
        modifier = Modifier.padding(top = 64.dp)
    ) {
        items(installedApps) { app ->
            AppItem(app, viewModel)
        }
    }
}

@Composable
fun AppItem(
    app: InstalledApp,
    viewModel: AppLockViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // initialize as false,
    // then update the value from launched effect (coroutine scope)
    var isAppLocked by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = app.packageName) {
        val status = viewModel.getLockStatus(app.packageName)
        isAppLocked = status
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(horizontal = 8.dp)
            .clickable {
                // Toggle the checkbox state and update the UI
                isAppLocked = !isAppLocked
            },
        shape = RoundedCornerShape(22.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    app.icon?.let { icon ->
                        Image(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(corner = CornerSize(16.dp))),

                            painter = BitmapPainter(icon),
                            contentDescription = null,
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            color = MaterialTheme.colorScheme.primary,
                            text = app.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Text(
                            text = app.packageName,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }

                Switch(
                    modifier = Modifier.padding(end = 8.dp),
                    checked = isAppLocked,
                    onCheckedChange = { isChecked ->
                        isAppLocked = isChecked
                        if (isChecked) {
                            Toast.makeText(context, "App Successfully Locked!", Toast.LENGTH_LONG).show()
                            coroutineScope.launch {
                                withContext(Dispatchers.IO) {
                                    viewModel.lockApplication(context, app.packageName, true)
                                }
                            }
                        } else {
                            Toast.makeText(context, "App Successfully Unlocked!", Toast.LENGTH_LONG).show()
                            coroutineScope.launch {
                                withContext(Dispatchers.IO) {
                                    viewModel.lockApplication(context, app.packageName, false)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}