package com.example.parentcontrolapp.model

import androidx.compose.ui.graphics.ImageBitmap

data class InstalledApp (
    val name: String,
    val packageName: String,
    val icon: ImageBitmap?,
    var screenTime: Long,
)

