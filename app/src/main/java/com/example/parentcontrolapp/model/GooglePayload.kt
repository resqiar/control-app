package com.example.parentcontrolapp.model

data class GooglePayload(
    val sub: String,
    val given_name: String,
    val display_name: String,
    val email: String,
    val picture: String,
)
