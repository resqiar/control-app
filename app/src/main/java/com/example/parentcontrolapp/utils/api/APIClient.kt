package com.example.parentcontrolapp.utils.api

import com.example.parentcontrolapp.model.GooglePayload
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
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

interface ApiService {
    @POST("/mobile/sync-user")
    fun syncUser(@Body() payload: GooglePayload): Call<TokenResponse>
}