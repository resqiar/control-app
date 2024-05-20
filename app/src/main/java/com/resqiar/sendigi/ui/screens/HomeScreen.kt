package com.resqiar.sendigi.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
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
import com.resqiar.sendigi.ui.theme.AppTheme



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
                .verticalScroll(rememberScrollState())
            ) {
                Text(text = "Selamat Datang!", fontSize = 24.sp)

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
                            text = "Pantau dan kelola penggunaan gadget anak-anak menggunakan tools kami.",
                            modifier = Modifier
                                .padding(6.dp),
                            textAlign = TextAlign.Left,
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
                                text = "Statistik Penggunaan Aplikasi",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Left,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 20.sp
                            )

                            Text(
                                text = "Fitur ini memastikan Anda dapat melihat total waktu penggunaan aplikasi anak Anda secara real-time.",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Justify,
                                fontSize = 14.sp
                            )
                            Button(onClick = {
                                navController.navigate(Screens.AppsUsage.screens)
                            }, Modifier.padding(2.dp),) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
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
                                text = "Kunci Aplikasi Tertentu",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Left,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 20.sp
                            )

                            Text(
                                text = "Melalui pengunci aplikasi kami, Anda dapat mengamankan atau membuka kunci aplikasi tertentu secara mudah.",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Justify,
                                fontSize = 14.sp
                            )
                            Button(onClick = {
                                navController.navigate(Screens.AppLock.screens)
                            }, Modifier.padding(2.dp),) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
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
                                text = "Penjadwalan Kunci",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Left,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 20.sp
                            )

                            Text(
                                text = "Melalui fitur ini, Anda dapat dengan mudah menyetel dan menyesuaikan batasan waktu penggunaan gadget.",
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Justify,
                                fontSize = 14.sp
                            )
                            Button(onClick = {
                                navController.navigate(Screens.AppLockScheduler.screens)
                            }, Modifier.padding(2.dp),) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "add",)
                            }
                        }
                    }
                }

            }
        }
    }

}