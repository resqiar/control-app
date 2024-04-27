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
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.coroutineScope

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val IS_LOGGED_IN_KEY = "is_logged_in"
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
                                val token = tokenCredential.idToken

                                Log.d("TOKEN", token)

                                _isLoggedIn.value = true
                                setLoggedIn(context)
                                Toast.makeText(context, "You are signed in!", Toast.LENGTH_LONG).show()

                                context.startActivity(
                                    Intent(context, MainActivity::class.java)
                                )
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
        return context.getSharedPreferences(IS_LOGGED_IN_KEY, Context.MODE_PRIVATE)
            .getBoolean(IS_LOGGED_IN_KEY, false)
    }

    private fun setLoggedIn(context: Context) {
        context.getSharedPreferences(IS_LOGGED_IN_KEY, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(IS_LOGGED_IN_KEY, true)
            .apply()
    }
}