package com.example.parentcontrolapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentcontrolapp.model.InstalledApp
import com.example.parentcontrolapp.ui.theme.AppTheme
import com.example.parentcontrolapp.utils.getInstalledApps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun InstalledAppsList(apps: ArrayList<InstalledApp>, onItemClick: (InstalledApp) -> Unit = {}) {
    AppTheme {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ),
            shape = RoundedCornerShape(22.dp),

            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, bottom = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(imageVector = Icons.Filled.Info, contentDescription = "Info", modifier = Modifier.padding(6.dp))
                Text(
                    text = "You could pick the preferred app to schedule for lock here.",
                    modifier = Modifier
                        .padding(6.dp),
                    textAlign = TextAlign.Justify,
                    fontSize = 14.sp
                )
            }

        }
        LazyColumn {

            items(apps.size) { idx ->

                Card(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .fillMaxWidth().clickable { onItemClick(apps[idx]) },
                    shape = RoundedCornerShape(corner = CornerSize(16.dp))
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        apps[idx].icon?.let { icon ->
                            Image(
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(corner = CornerSize(16.dp))),

                                painter = BitmapPainter(icon),
                                contentDescription = null,
                            )
                        }

                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically)
                        ) {
                            Text(
                                color = MaterialTheme.colorScheme.primary,
                                text = apps[idx].name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            Text(
                                text = apps[idx].packageName,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}