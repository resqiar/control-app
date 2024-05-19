package com.resqiar.sendigi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.resqiar.sendigi.dao.AppInfoDao
import com.resqiar.sendigi.entities.AppInfoEntity

@Database(
    entities = [AppInfoEntity::class],
    version = 1
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun appInfoDao(): AppInfoDao
}