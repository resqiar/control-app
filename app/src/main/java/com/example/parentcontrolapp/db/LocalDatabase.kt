package com.example.parentcontrolapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parentcontrolapp.dao.AppInfoDao
import com.example.parentcontrolapp.entities.AppInfoEntity

@Database(
    entities = [AppInfoEntity::class],
    version = 1
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun appInfoDao(): AppInfoDao
}