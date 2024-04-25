package com.example.parentcontrolapp.viewModel

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.parentcontrolapp.model.InstalledApp
import com.example.parentcontrolapp.utils.getInstalledApps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppLockViewModel(application: Application) : AndroidViewModel(application) {
    private val lockedAppsPrefKey = "locked_apps"

    private val _installedApps = MutableLiveData<ArrayList<InstalledApp>>()
    val installedApps: LiveData<ArrayList<InstalledApp>> get() = _installedApps
    private fun setInstalledApps(apps: ArrayList<InstalledApp>) {
        _installedApps.postValue(apps)
    }

    init {
        init()
    }

    // Init function to get all installed apps inside lock app screen
    private fun init() {
        viewModelScope.launch(Dispatchers.Default) {
            val installedApps = getInstalledApps(
                getApplication<Application>().applicationContext
            )
            setInstalledApps(installedApps)
        }
    }

    // Function to lock an app
    fun lockApp(context: Context, packageName: String) {
        context.getSharedPreferences(lockedAppsPrefKey, Context.MODE_PRIVATE).edit {
            putBoolean(packageName, true) // Set the lock status of the app to true
        }
    }

    // Function to unlock an app
    fun unlockApp(context: Context, packageName: String) {
        context.getSharedPreferences(lockedAppsPrefKey, Context.MODE_PRIVATE).edit {
            putBoolean(packageName, false) // Set the lock status of the app to false
        }
    }

    // Function to check if an app is locked
    fun isAppLocked(context: Context, packageName: String): Boolean {
        val prefs = context.getSharedPreferences(lockedAppsPrefKey, Context.MODE_PRIVATE)
        return prefs.getBoolean(packageName, false) // Return true if the app is locked, false otherwise
    }

    // Function to get list of all locked apps
    fun getAllLockedApps(context: Context): List<String> {
        val prefs = context.getSharedPreferences(lockedAppsPrefKey, Context.MODE_PRIVATE)
        return prefs.all.filterValues { it == true }.keys.toList()
    }
}