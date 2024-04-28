package com.example.parentcontrolapp.viewModel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.parentcontrolapp.MainActivity
import com.example.parentcontrolapp.constants.Constants
import com.example.parentcontrolapp.model.GooglePayload
import com.example.parentcontrolapp.utils.api.ApiClient
import com.example.parentcontrolapp.utils.api.TokenResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.coroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>()
    private val _isLoggedIn = MutableLiveData(isLoggedIn(context = context))
    val isLoggedIn: MutableLiveData<Boolean> = _isLoggedIn

    suspend fun login(context: Context) {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("857821872497-2c3uhqee548ubhisdkr8p8rhru4ls2sh.apps.googleusercontent.com")
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )

                when (val credential = result.credential) {
                    is CustomCredential -> {
                        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            try {
                                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                val data = tokenCredential.data

                                Log.d("TOKEN", data.toString())

                                // send data to the server to get JWT token
                                val call = ApiClient.apiService.syncUser(GooglePayload(
                                    given_name = tokenCredential.givenName ?: "",
                                    display_name = tokenCredential.displayName ?: "",
                                    sub = tokenCredential.idToken,
                                    email = tokenCredential.id,
                                    picture = tokenCredential.profilePictureUri.toString()
                                ))

                                call.enqueue(object: Callback<TokenResponse> {
                                    override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                                        if (response.isSuccessful) {
                                            val post = response.body()

                                            // save token to shared preference
                                            _isLoggedIn.value = true
                                            setLoggedIn(context, post?.token ?: "")

                                            Toast.makeText(context, "Successfully signed in!", Toast.LENGTH_LONG).show()

                                            context.startActivity(
                                                Intent(context, MainActivity::class.java)
                                            )
                                        } else {
                                            Log.d("HTTP REQUEST", response.toString())
                                        }
                                    }

                                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                                        Log.d("HTTP FAILURE", t.message.toString())
                                    }
                                })
                            } catch (e: GoogleIdTokenParsingException) {
                                Log.e("PARSING ERROR", "Received an invalid google id token response", e)
                            }
                        } else {
                            Log.e("AUTH ERROR", "Unexpected type of credential")
                        }
                    }
                    else -> {
                        Log.e("OTHER ERROR", "Unexpected type of credential")
                    }
                }
            } catch (e: androidx.credentials.exceptions.GetCredentialException) {
                Log.e("AUTH ERROR", e.toString())
            }
        }
    }

    private fun isLoggedIn(context: Context): Boolean {
        val status =  context.getSharedPreferences(Constants.LOG_TOKEN_PREF, Context.MODE_PRIVATE)
            .getString(Constants.LOG_TOKEN_PREF, "") ?: ""
        return status.isNotEmpty()
    }

    private fun setLoggedIn(context: Context, token: String) {
        context.getSharedPreferences(Constants.LOG_TOKEN_PREF, Context.MODE_PRIVATE)
            .edit()
            .putString(Constants.LOG_TOKEN_PREF, token)
            .apply()
    }
}