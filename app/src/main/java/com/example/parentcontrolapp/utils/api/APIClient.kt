package com.example.parentcontrolapp.utils.api

import com.example.parentcontrolapp.model.AppInfo
import com.example.parentcontrolapp.model.DeviceInfo
import com.example.parentcontrolapp.model.GooglePayload
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8888"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object ApiClient {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}

data class TokenResponse(
    @SerializedName("token")
    val token: String
)

data class StatusResponse(
    @SerializedName("status")
    val status: Int
)

interface ApiService {
    @POST("/mobile/sync-user")
    fun syncUser(@Body() payload: GooglePayload): Call<TokenResponse>

    @POST("/mobile/sync-device")
    fun syncDevice(
        @Header("Authorization") token: String,
        @Body() payload: DeviceInfo
    ): Call<StatusResponse>

    @POST("/mobile/sync-app")
    fun syncApp(
        @Header("Authorization") token: String,
        @Body() payload: AppInfo
    ): Call<StatusResponse>
}