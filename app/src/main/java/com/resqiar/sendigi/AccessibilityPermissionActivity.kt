package com.resqiar.sendigi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.resqiar.sendigi.ui.theme.AppTheme
import com.resqiar.sendigi.ui.screens.MinimalDialog
import com.resqiar.sendigi.utils.askAccessibilityPermission
import com.resqiar.sendigi.utils.checkAccessibility
import kotlin.system.exitProcess

class AccessibilityPermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val styledDescription = buildAnnotatedString {
            append("To use this app effectively, please grant the necessary permissions:\n\n")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Accessibility Permission:")
            }

            append("\n1. Open your device settings.\n2. Navigate to ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"Accessibility\".")
            }

            append("\n3. Find and select ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"SenDigi\".")
            }

            append("\n4. Tap ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"Enable Service\".")
            }

            append("\n5. Toggle the switch for ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"SenDigi\".")
            }

            append("\n\n")

            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                append("This permission is necessary to enable the locking feature effectively.")
            }
        }

        setContent {
            AppTheme {
                MinimalDialog(
                    title = "Accessibility Permissions Required",
                    styledDescription,
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