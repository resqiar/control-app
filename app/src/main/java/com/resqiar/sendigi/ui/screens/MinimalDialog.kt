package com.resqiar.sendigi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun MinimalDialog(
    title: String,
    description: String,
    cancelText: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
           modifier = Modifier
               .fillMaxWidth()
               .height(IntrinsicSize.Min),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = description
                )

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp)
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(width = 60.dp, height = 30.dp),
                        onClick = onDismiss,

                    ) {
                        Text(
                            text = cancelText,
                            fontSize = 10.sp
                        )
                    }

                    Button(
                        modifier = Modifier.padding(0.dp).size(width = 120.dp, height = 30.dp),
                        onClick = onConfirm,
                    ) {
                        Text(
                            text = confirmText,

                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MinimalDialogPreview() {
    MinimalDialog(
        title = "\uD83D\uDD12 Permissions Required! \uD83D\uDD12",
        description = """
                        To use this app effectively, please grant the necessary permissions:

                        Usage Stats Permission:

                        1. Open your device settings.
                        2. Navigate to 'Apps' or 'Application Manager.'
                        3. Find and select 'Your App Name.'
                        4. Tap 'Permissions.'
                        5. Enable 'Usage Access' or 'Usage Stats.'

                        Granting this permission allows us to provide you with valuable insights and features. Thank you for your cooperation!
                    """.trimIndent(),
        cancelText = "Exit",
        confirmText = "Go to Settings",
        onDismiss = {},
        onConfirm = {},
    )
}