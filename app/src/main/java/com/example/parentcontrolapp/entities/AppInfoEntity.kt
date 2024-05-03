package com.example.parentcontrolapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "app_info",
    indices = [Index(value = ["package_name"], unique = true)]
)
data class AppInfoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "package_name")
    val packageName: String,

    @ColumnInfo(name = "lock_status")
    val lockStatus: Boolean = false,

    @ColumnInfo(name = "icon")
    val icon: String? = null,

    @ColumnInfo(name = "time_usage")
    val timeUsage: Long = 0
)