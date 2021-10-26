package com.topchu.recoverfrombreakup.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "tasks")
data class TaskEntity(
    val title: String,
    val subtitle: String,
    val texts: List<Int>,
    @ColumnInfo(name = "is_opened") val isOpened: Boolean = false,
    @ColumnInfo(name = "is_active") val isActive: Boolean = false,
    @ColumnInfo(name = "is_blocked") val isBlocked: Boolean = false,
    @ColumnInfo(name = "has_meditation") val hasMeditation: Boolean = false,
    @ColumnInfo(name = "meditation_id") val meditationId: Long? = null,
    @ColumnInfo(name = "will_open_at") val willOpenAt: Long? = null,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
)