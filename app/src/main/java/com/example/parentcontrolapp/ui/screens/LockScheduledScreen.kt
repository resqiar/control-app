package com.example.parentcontrolapp.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentcontrolapp.R
import com.example.parentcontrolapp.ui.theme.AppTheme

@Composable
fun LockScheduledScreen(pkgName: String, dates: Array<String>?, startTime: String?, endTime: String?) {
    val ctx = LocalContext.current

    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_sendigi),
                contentDescription = "Logo SenDigi",
                modifier = Modifier
                    .width(400.dp)
                    .height(400.dp)
                    .padding(bottom = 8.dp)
            )

            // only show text when parent scheduled by Date
            if (!dates.isNullOrEmpty()) {
                Text(
                    text = "This Application is Locked by Your Parent",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,

                    )
                )
                Text(
                    text = "From ${dates.first()} until ${dates.last()}",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                    )
                )

            }

            // only show text when parent scheduled by Time
            if (!startTime.isNullOrEmpty() && !endTime.isNullOrEmpty()) {
                Text(
                    text = "This Application is Locked by Your Parent",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                    )
                )
                Text(
                    text = "From $startTime until $endTime",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                    )
                )
            }

            Text(
                text = "Use your time and device more responsibly.",
                modifier = Modifier.padding(bottom = 24.dp),
                style = TextStyle(textAlign = TextAlign.Center,)
            )
            Button(onClick = {
                exitToHome(ctx, pkgName)
            }) {
                Text("Close the Application")
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
