package com.resqiar.sendigi.ui.screens

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resqiar.sendigi.R
import com.resqiar.sendigi.ui.theme.AppTheme


@Composable
fun LockScreen(pkgName: String) {
    val ctx = LocalContext.current

    AppTheme {
        Surface (color = Color.White) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(id = R.drawable.security),
                    contentDescription = "Logo SenDigi",
                    modifier = Modifier
                        .width(400.dp)
                        .height(400.dp)
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = "Aplikasi ini Terkunci!",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,

                        )
                )
                Text(
                    text = "Gunakan waktu dan perangkat Anda dengan lebih bertanggung jawab.",
                    modifier = Modifier.padding(bottom = 24.dp),
                    textAlign = TextAlign.Center,
                )
                Button(onClick = {
                    exitToHome(ctx, pkgName)
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.padding(start = 4.dp, end = 4.dp))
                    Text("Tutup Aplikasi", Modifier.padding(start = 4.dp, end = 4.dp))

                }
            }
        }

    }
}


private fun exitToHome(ctx: Context, pkgName: String) {
    // close the locked app
    val manager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    manager.killBackgroundProcesses(pkgName)

    // close the current app
    if (ctx is Activity) {
        ctx.finish()
    }

    // go to home screen
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    ctx.startActivity(intent)
}

@Preview
@Composable
fun LockScreenPreview() {
    LockScreen("")
}
