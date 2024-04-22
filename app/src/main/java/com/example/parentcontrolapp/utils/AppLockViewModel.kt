import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel

class AppLockViewModel : ViewModel() {
    private val lockedAppsPrefKey = "locked_apps"

    // Function to lock an app
    fun lockApp(context: Context, packageName: String) {
        Log.d("AppLockViewModel", "Locking app: $packageName")
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

    fun getAllInstalledApps(context: Context): List<String> {
        val packageManager = context.packageManager
        val installedApps = mutableListOf<String>()

        // Get a list of all installed packages
        val packages = packageManager.getInstalledPackages(0)

        // Iterate through the list of packages and add package names to the list
        for (packageInfo in packages) {
            installedApps.add(packageInfo.packageName)
        }

        return installedApps
    }

}
