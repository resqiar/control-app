package com.resqiar.sendigi.model

data class RequestMessage(
    val message: String,
    val packageName: String,
    val deviceId: String
)

data class WebRequestMessage (
    val message: String,
)