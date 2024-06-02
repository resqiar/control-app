package com.resqiar.sendigi.utils.api

import com.resqiar.sendigi.constants.Constants
import com.resqiar.sendigi.model.AppInfo
import com.resqiar.sendigi.model.DeviceActivity
import com.resqiar.sendigi.model.DeviceInfo
import com.resqiar.sendigi.model.GooglePayload
import com.google.gson.annotations.SerializedName
import com.resqiar.sendigi.model.RequestMessage
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

object RetrofitClient {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.HTTP_SERVER_URL)
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
    @SerializedName("id")
    val id: String,
    @SerializedName("token")
    val token: String
)

data class StatusResponse(
    @SerializedName("status")
    val status: Int
)

interface ApiService {
    @POST("/mobile/sync-user")
    fun syncUser(@Body payload: GooglePayload): Call<TokenResponse>

    @POST("/mobile/sync-device")
    fun syncDevice(
        @Header("Authorization") token: String,
        @Body payload: DeviceInfo
    ): Call<StatusResponse>

    @POST("/mobile/sync-device-activity")
    fun syncDeviceActivity(
        @Header("Authorization") token: String,
        @Body payload: DeviceActivity
    ): Call<StatusResponse>

    @POST("/mobile/sync-app")
    fun syncApp(
        @Header("Authorization") token: String,
        @Body payload: AppInfo
    ): Call<StatusResponse>

    @POST("/mobile/message/request")
    fun requestMessage(
        @Header("Authorization") token: String,
        @Body payload: RequestMessage
    ): Call<StatusResponse>
}