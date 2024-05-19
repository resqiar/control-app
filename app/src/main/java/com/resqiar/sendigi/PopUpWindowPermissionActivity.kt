package com.resqiar.sendigi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.resqiar.sendigi.ui.theme.AppTheme
import com.resqiar.sendigi.ui.screens.MinimalDialog
import com.resqiar.sendigi.utils.XiaomiUtilities

class PopUpWindowPermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val styledDescription = buildAnnotatedString {
            append("Xiaomi requires you to enable \"")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Display pop-up while in background")
            }
            append("\" permission. \n\n")
            append("""
                You may enable this by clicking Go to Settings button, otherwise, the locking feature may not be able to work in Xiaomi devices.
            """.trimIndent())
        }

        setContent {
            AppTheme {
                MinimalDialog(
                    title = "You are using Xiaomi devices",
                    styledDescription,
                    cancelText = "Back",
                    confirmText = "Go to Settings",
                    onDismiss = {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    },
                    onConfirm = {
                        val intent = XiaomiUtilities.getPermissionManagerIntent(this)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    },
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
    if (XiaomiUtilities.isCustomPermissionGranted(ctx, XiaomiUtilities.OP_BACKGROUND_START_ACTIVITY)) {
        val intent = Intent(ctx, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ctx.startActivity(intent)
    }
}
