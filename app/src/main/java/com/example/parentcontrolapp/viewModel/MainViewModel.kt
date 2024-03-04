package com.example.parentcontrolapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.parentcontrolapp.model.InstalledApp
import com.example.parentcontrolapp.utils.getInstalledApps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val _installedApps = MutableLiveData<ArrayList<InstalledApp>>()
    val installedApps: LiveData<ArrayList<InstalledApp>> get() = _installedApps

    init {
        startPeriodicUpdates()
    }

    private fun setInstalledApps(apps: ArrayList<InstalledApp>) {
        _installedApps.postValue(apps)
    }

    private fun startPeriodicUpdates() {
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                val updatedApps = getInstalledApps(getApplication<Application>().applicationContext)
                setInstalledApps(updatedApps)
                delay(60 * 1000) // 60 seconds
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}