package com.resqiar.sendigi

import android.app.Application
import androidx.room.Room
import com.resqiar.sendigi.constants.Constants
import com.resqiar.sendigi.db.LocalDatabase

class ApplicationActivity: Application() {
    companion object {
        private lateinit var database: LocalDatabase

        fun getInstance(): LocalDatabase {
            return database
        }
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            this,
            LocalDatabase::class.java,
            Constants.LOCAL_DATABASE
        ).build()
    }
}