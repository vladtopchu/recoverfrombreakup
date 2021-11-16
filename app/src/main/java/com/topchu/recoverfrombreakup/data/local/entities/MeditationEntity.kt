package com.topchu.recoverfrombreakup.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "meditations")
data class MeditationEntity(
    @ColumnInfo() val name: String,
    @ColumnInfo() val uri: String,
    @ColumnInfo(name = "task_id") val taskId: Long = 0L,
    @ColumnInfo(name = "is_opened") val isOpened: Boolean = false,
    @ColumnInfo(name = "is_blocked") val isBlocked: Boolean = true,
    @ColumnInfo(name = "cached_uri") val cachedUri: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
)