package com.example.parentcontrolapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.parentcontrolapp.entities.AppInfoEntity

@Dao
interface AppInfoDao {
    @Query("SELECT * FROM app_info")
    suspend fun getAllAppInfo(): List<AppInfoEntity>

    @Query("SELECT * FROM app_info WHERE package_name = :packageName LIMIT 1")
    suspend fun getAppInfo(packageName: String): AppInfoEntity?

    @Insert()
    suspend fun createAppInfo(info: AppInfoEntity)

    @Update()
    suspend fun updateAppInfo(info: AppInfoEntity)

    @Query("UPDATE app_info SET lock_status = :lockStatus WHERE package_name = :packageName")
    suspend fun updateLock(packageName: String, lockStatus: Boolean)

    @Query("SELECT lock_status FROM app_info WHERE package_name = :packageName")
    suspend fun getLockStatus(packageName: String): Boolean

    @Query("SELECT package_name FROM app_info")
    suspend fun getLockedApps(): List<String>

    @Query(
        "UPDATE app_info SET lock_dates = :lockDates, lock_start_time = :lockStartTime, lock_end_time = :lockEndTime WHERE package_name = :packageName"
    )
    suspend fun updateScheduler(packageName: String, lockDates: String, lockStartTime: String, lockEndTime: String)
}