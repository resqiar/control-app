package com.resqiar.sendigi.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.resqiar.sendigi.ApplicationActivity
import com.resqiar.sendigi.model.InstalledApp
import com.resqiar.sendigi.utils.api.sendApplicationDataWithDeviceData
import com.resqiar.sendigi.utils.getDeviceInstalledApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppLockViewModel(application: Application) : AndroidViewModel(application) {
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
            val installedApps = getDeviceInstalledApplication(
                getApplication<Application>().applicationContext
            )
            setInstalledApps(installedApps)
        }
    }

    // Function to lock/unlock an app
    suspend fun lockApplication(ctx: Context, packageName: String, status: Boolean) {
        val infoDao = ApplicationActivity.getInstance().appInfoDao()
        withContext(Dispatchers.IO) {
            infoDao.updateLock(packageName, status)

            // send updated data in the background
            sendApplicationDataWithDeviceData(ctx)
        }
    }

    // Function to check if an app is locked
    suspend fun getLockStatus(packageName: String): Boolean {
        val infoDao = ApplicationActivity.getInstance().appInfoDao()
        return withContext(Dispatchers.IO) {
            infoDao.getLockStatus(packageName)
        }
    }

    // Function to get list of all locked apps
    suspend fun getAllLockedApps(): List<String> {
        val infoDao = ApplicationActivity.getInstance().appInfoDao()
        return withContext(Dispatchers.IO) {
            infoDao.getLockedApps()
        }
    }
}