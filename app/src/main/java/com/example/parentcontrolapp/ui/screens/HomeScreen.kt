package com.example.parentcontrolapp.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parentcontrolapp.ui.theme.AppTheme



@Composable
fun HomeScreen(navController: NavController) {
    AppTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 62.dp)
            .padding(24.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
            ) {
                Text(text = "Welcome", fontSize = 28.sp)

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ),
                    shape = RoundedCornerShape(22.dp),

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 22.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Info, contentDescription = "Info", modifier = Modifier.padding(6.dp))
                        Text(
                            text = "Your trusted solution to monitor and manage children's gadget usage.",
                            modifier = Modifier
                                .padding(6.dp),
                            textAlign = TextAlign.Justify,
                            fontSize = 14.sp
                        )
                    }

                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                ) {
                    Box(modifier = Modifier.padding(12.dp)) {
                        Column {
                            Text(
                                text = "Apps Usage Stats",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Justify,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 20.sp
                            )

                            Text(
                                text = "This feature ensure that you can see your child's app usage total time in real-time.",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Justify,
                                fontSize = 14.sp
                            )
                            Button(onClick = {
                                navController.navigate(Screens.AppsUsage.screens)
                            }, Modifier.padding(4.dp),) {
                                Icon(imageVector = Icons.Filled.KeyboardArrowRight,
                                    contentDescription = "add",)
                            }
                        }

                    }

                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Column {
                            Text(
                                text = "Lock Specific Application",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Justify,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 20.sp
                            )

                            Text(
                                text = "Through our app locker, you can gain the ability to secure or unlock certain apps remotely.",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Justify,
                                fontSize = 14.sp
                            )
                            Button(onClick = {
                                navController.navigate(Screens.AppLock.screens)
                            }, Modifier.padding(4.dp),) {
                                Icon(imageVector = Icons.Filled.KeyboardArrowRight,
                                    contentDescription = "add",)
                            }
                        }
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Column {
                            Text(
                                text = "Lock Scheduler",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Justify,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 20.sp
                            )

                            Text(
                                text = "Through this feature, you can easily set and adjust time limits of gadget usage.",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Justify,
                                fontSize = 14.sp
                            )
                            Button(onClick = {
                                navController.navigate(Screens.AppLockScheduler.screens)
                            }, Modifier.padding(4.dp),) {
                                Icon(imageVector = Icons.Filled.KeyboardArrowRight,
                                    contentDescription = "add",)
                            }
                        }
                    }
                }

            }
        }
    }

}