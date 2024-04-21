package com.example.parentcontrolapp.ui.screens

sealed class Screens(val screens: String) {
    data object Home: Screens("Home")
    data object AppsUsage: Screens("AppsUsage")
    data object AppLock: Screens("AppLock")
    data object AppLockScheduler: Screens("AppLockScheduler")
}