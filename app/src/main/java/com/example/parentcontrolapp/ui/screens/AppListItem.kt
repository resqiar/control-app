package com.example.parentcontrolapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.example.parentcontrolapp.model.InstalledApp

@Composable
fun AppList(metadata: InstalledApp) {
    AppTheme {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                metadata.icon?.let { icon ->
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
                        text = metadata.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(
                        text = metadata.packageName,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(
                        text = "${metadata.screenTime.first} Hours ${metadata.screenTime.second} Minutes",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }


}

@Preview
@Composable
fun AppListPreview() {
    AppList(metadata = InstalledApp("Test", "test.test@com", null, 0, screenTime = Pair(0, 0)))
}