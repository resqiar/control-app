package com.example.parentcontrolapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.parentcontrolapp.ui.screens.MinimalDialog
import com.example.parentcontrolapp.utils.askUsageStatsPermission
import com.example.parentcontrolapp.utils.getUsageStatsPermission
import kotlin.system.exitProcess

class PermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkIfPermitted(this)

        setContent {
            MinimalDialog(
                title = "\uD83D\uDD12 Permissions Required! \uD83D\uDD12",
                description = """
                        To use this app effectively, please grant the necessary permissions:

                        Usage Stats Permission:

                        1. Open your device settings.
                        2. Navigate to 'Apps' or 'Application Manager.'
                        3. Find and select 'Your App Name.'
                        4. Tap 'Permissions.'
                        5. Enable 'Usage Access' or 'Usage Stats.'

                        Granting this permission allows us to provide you with valuable insights and features. Thank you for your cooperation!
                    """.trimIndent(),
                cancelText = "Exit",
                confirmText = "Go to Settings",
                onDismiss = {
                    finish()
                    exitProcess(0)
                },
                onConfirm = { askUsageStatsPermission(this) },
            )
        }
    }

    override fun onResume() {
        super.onResume()
        checkIfPermitted(this)
    }

    override fun onRestart() {
        super.onRestart()
        checkIfPermitted(this)
    }
}

private fun checkIfPermitted(ctx: Context) {
    if (getUsageStatsPermission(ctx)) {
        val intent = Intent(ctx, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ctx.startActivity(intent)
    }
}