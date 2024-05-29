package com.resqiar.sendigi.ui.screens

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resqiar.sendigi.R
import com.resqiar.sendigi.ui.theme.AppTheme

@Composable
fun LockScheduledScreen(pkgName: String, dates: Array<String>?, startTime: String?, endTime: String?) {
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
                    painter = painterResource(id = R.drawable.time),
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
                            fontSize = 14.sp,
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
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    )
                }

                Text(
                    text = "Use your time and device more responsibly.",
                    modifier = Modifier.padding(bottom = 24.dp),
                    style = TextStyle(textAlign = TextAlign.Center)
                )
                var showTextField by remember { mutableStateOf(false) }
                var textFieldValue by remember { mutableStateOf("") }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row {
                        Button(onClick = {
                            exitToHome(ctx, pkgName)
                        }, modifier = Modifier
                            .padding(top = 18.dp, end = 2.dp)
                            .weight(1f)) {
//                            Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.padding(start = 4.dp, end = 4.dp))
                            Text("Close Application", Modifier.padding(start = 4.dp, end = 4.dp))

                        }
                        Button(
                            modifier = Modifier
                                .padding(top = 18.dp, start = 2.dp)
                                .weight(1f),
                            onClick = { showTextField = !showTextField },
                        ) {
                            Text("Request Unlock")
                        }
                    }


                    if (showTextField) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedTextField(
                                value = textFieldValue,
                                onValueChange = { textFieldValue = it },
                                modifier = Modifier.weight(1f),
                                label = { Text("Enter your reason") }
                            )
                            Button(
                                onClick = { handleSendAction(textFieldValue, ctx)},
                                modifier = Modifier.padding(start = 8.dp, top = 12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Send, contentDescription = null)

                            }
                        }
                    }
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

private fun handleSendAction(textFieldValue: String, context: Context) {

    println(textFieldValue)
    Toast.makeText(context, "Request sent successfully", Toast.LENGTH_SHORT).show()
}
