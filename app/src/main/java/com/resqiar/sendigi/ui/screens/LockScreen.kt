package com.resqiar.sendigi.ui.screens

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.resqiar.sendigi.utils.api.sendRequestMessage
import kotlinx.coroutines.launch


@Composable
fun LockScreen(pkgName: String) {
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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
                    text = "This Application is Locked by Your Parent",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,

                        )
                )
                Text(
                    text = "Use your time and device more responsibly.",
                    modifier = Modifier.padding(bottom = 24.dp),
                    textAlign = TextAlign.Center,
                )
                var showTextField by remember { mutableStateOf(false) }
                var textFieldValue by remember { mutableStateOf("") }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (!showTextField) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 18.dp, start = 18.dp, end = 18.dp)
                                .height(IntrinsicSize.Min),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    exitToHome(ctx, pkgName)
                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                )
                                Text("Close Application", Modifier.padding(start = 4.dp, end = 4.dp))

                            }
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.Gray,
                                ),
                                onClick = { showTextField = !showTextField },
                            ) {
                                Text("Request Unlock")
                            }
                        }
                    }

                    if (showTextField) {
                        Column {
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
                                    onClick = {
                                        coroutineScope.launch {
                                            handleSendAction(pkgName, textFieldValue, ctx)
                                            textFieldValue = ""
                                        }
                                    },
                                    modifier = Modifier.padding(start = 8.dp, top = 12.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Send, contentDescription = null)
                                }
                            }

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.Gray,
                                ),
                                onClick = { showTextField = !showTextField },
                            ) {
                                Text("Close Request")
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

private suspend fun handleSendAction(packageName: String, textFieldValue: String, context: Context) {
    sendRequestMessage(textFieldValue, packageName, context)
    Toast.makeText(context, "Request sent, please wait for further notification", Toast.LENGTH_SHORT).show()
}

@Preview
@Composable
fun LockScreenPreview() {
    LockScreen("")
}
