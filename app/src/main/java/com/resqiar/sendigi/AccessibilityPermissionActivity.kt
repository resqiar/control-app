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
            append("Untuk menggunakan aplikasi ini secara efektif, mohon berikan izin yang diperlukan:\n\n")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Izin Aksesibilitas:")
            }

            append("\n1. Buka pengaturan perangkat Anda.\n2. Navigasi ke ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"Aksesibilitas\".")
            }

            append("\n3. Cari dan pilih ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"SenDigi\".")
            }

            append("\n4. Ketuk ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"Aktifkan Layanan\".")
            }

            append("\n5. Ketuk switch untuk ")

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\"SenDigi\".")
            }

            append("\n\n")

            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                append("Izin ini diperlukan untuk mengaktifkan fitur penguncian secara efektif.")
            }
        }

        setContent {
            AppTheme {
                MinimalDialog(
                    title = "Dibutuhkan Izin Aksesibilitas",
                    styledDescription,
                    cancelText = "Keluar",
                    confirmText = "Pergi ke Settings",
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