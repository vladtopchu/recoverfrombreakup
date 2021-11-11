package com.topchu.recoverfrombreakup.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "notifications")
data class NotificationEntity(
    val title: String,
    val text: String,
    val timestamp: Long,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
)