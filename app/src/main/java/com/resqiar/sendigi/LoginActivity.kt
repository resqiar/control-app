package com.resqiar.sendigi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.resqiar.sendigi.ui.theme.AppTheme
import com.resqiar.sendigi.viewModel.AuthViewModel
import kotlinx.coroutines.launch

class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginContent()
        }
    }
}

@Composable
fun LoginContent() {
    AppTheme {
        val viewModel: AuthViewModel = viewModel()
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        Surface {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(16.dp),

                ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_sendigi),
                    contentDescription = "Logo SenDigi",
                    modifier = Modifier
                        .width(400.dp)
                        .height(400.dp)
                        .padding(bottom = 4.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(start = 22.dp, end = 22.dp)
                ) {

                    Text(text = "Log In", color = MaterialTheme.colorScheme.primary, fontSize = 38.sp)
                    Text(text = "You need to be logged in before using the app", fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.login(context)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = "Continue with Google")
                        }
                    }

                }

            }
        }
    }

}