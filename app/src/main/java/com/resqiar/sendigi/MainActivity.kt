package com.resqiar.sendigi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.resqiar.sendigi.ui.theme.AppTheme
import com.resqiar.sendigi.utils.BackgroundTaskUtil
import com.resqiar.sendigi.utils.RabbitMQService
import com.resqiar.sendigi.utils.checkAccessibility
import com.resqiar.sendigi.utils.getUsageStatsPermission
import com.resqiar.sendigi.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // authentication data coming from the view model
        val viewModel: AuthViewModel by viewModels()

        CoroutineScope(Dispatchers.IO).launch {
            handleAskPermissions(this@MainActivity)
        }

        // show splash screen while waiting for background process to ask permissions
        installSplashScreen()

        setContent {
            AppTheme {
                if (viewModel.isLoggedIn.value == true) {
                    NavDrawer()
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}

private fun handleAskPermissions(ctx: Context) {
    // check permissions for usage stats
    if (!getUsageStatsPermission(ctx)) {
        val intent = Intent(ctx, PermissionActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ctx.startActivity(intent)
    }

    // check permissions for accessibility
    else if (!checkAccessibility(ctx)) {
        val intent = Intent(ctx, AccessibilityPermissionActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ctx.startActivity(intent)
    }

    // if everything fine, trigger background task
    else {
        // Start the RabbitMQ listener service
        val intent = Intent(ctx, RabbitMQService::class.java)
        ctx.startService(intent)

        BackgroundTaskUtil.startBackgroundTask(ctx)
    }
}