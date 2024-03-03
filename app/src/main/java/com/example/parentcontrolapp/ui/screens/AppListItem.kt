package com.example.parentcontrolapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentcontrolapp.model.InstalledApp

@Composable
fun AppList(metadata: InstalledApp) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        metadata.icon?.let { icon ->
            Image(
                modifier = Modifier
                    .size(32.dp),
                painter = BitmapPainter(icon),
                contentDescription = null,
            )
        }

        Column {
            Text(
                text = metadata.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
            Text(
                text = metadata.packageName,
                fontSize = 13.sp,
            )
            Text(
                text = "${metadata.screenTime.toString()} Minute(s)",
                fontSize = 12.sp,
            )
        }
    }
}

@Preview
@Composable
fun AppListPreview() {
    AppList(metadata = InstalledApp("Test", "test.test@com", null, 0))
}