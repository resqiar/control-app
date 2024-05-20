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
import com.resqiar.sendigi.utils.askUsageStatsPermission
import com.resqiar.sendigi.utils.getUsageStatsPermission
import kotlin.system.exitProcess

class PermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val styledDescription = buildAnnotatedString {
            append("Untuk menggunakan aplikasi ini secara efektif, mohon berikan izin yang diperlukan:\n\n")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Izin Usage Stats:")
            }

            append("\n1. Buka pengaturan perangkat Anda.\n 2. Navigasi ke ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"Usage Access\"")
            }

            append(" atau ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"Application Manager\".")
            }

            append("\n3. Cari dan pilih ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"SenDigi\".")
            }

            append("\n4. Permit atau Enable ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"Usage Access\".")
            }

            append("\n\n")

            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                append("Memberikan izin ini memungkinkan kami untuk menyediakan data perangkat Anda.")
            }
        }

        setContent {
            AppTheme {
                MinimalDialog(
                    title = "Dibutuhkan Izin Usage Stats",
                    styledDescription,
                    cancelText = "Keluar",
                    confirmText = "Pergi ke Settings",
                    onDismiss = {
                        finish()
                        exitProcess(0)
                    },
                    onConfirm = { askUsageStatsPermission(this) },
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
    if (getUsageStatsPermission(ctx)) {
        val intent = Intent(ctx, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ctx.startActivity(intent)
    }
}