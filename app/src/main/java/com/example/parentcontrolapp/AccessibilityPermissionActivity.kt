package com.example.parentcontrolapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.parentcontrolapp.ui.theme.AppTheme
import com.example.parentcontrolapp.ui.screens.MinimalDialog
import com.example.parentcontrolapp.utils.askAccessibilityPermission
import com.example.parentcontrolapp.utils.checkAccessibility
import kotlin.system.exitProcess

class AccessibilityPermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkIfPermitted(this)

        setContent {
            AppTheme {
                MinimalDialog(
                    title = "\uD83D\uDD12 Permissions Required! \uD83D\uDD12",
                    description = """
                        To use this app effectively, please grant the necessary permissions:

                        Accessibility Permission:

                        1. Open your device settings.
                        2. Navigate to "Accessibility."
                        3. Find and select "SenDigi"
                        4. Tap "Enable Service."
                        5. Toggle the switch for "SenDigi"

                        This permission is necessary to enable the locking feature effectively. 
                    """.trimIndent(),
                    cancelText = "Exit",
                    confirmText = "Go to Settings",
                    onDismiss = {
                        finish()
                        exitProcess(0)
                    },
                    onConfirm = { askAccessibilityPermission(this) },
                )
            }

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
    if (checkAccessibility(ctx)) {
        val intent = Intent(ctx, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ctx.startActivity(intent)
    }
}