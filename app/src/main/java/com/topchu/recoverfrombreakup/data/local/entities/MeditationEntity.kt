package com.topchu.recoverfrombreakup.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "meditations")
data class MeditationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo() val name: String,
    @ColumnInfo() val uri: String,
    @ColumnInfo(name = "cached_uri") val cachedUri: String?,
    @ColumnInfo(name = "is_opened") val isOpened: Boolean,
    @ColumnInfo(name = "task_id") val taskId: Int
)



