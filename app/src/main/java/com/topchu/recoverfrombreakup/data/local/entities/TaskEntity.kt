package com.topchu.recoverfrombreakup.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "tasks")
data class TaskEntity(
    val title: String,
    val subtitle: String,
    @ColumnInfo(name = "is_opened") val isOpened: Boolean = false,
    @ColumnInfo(name = "is_blocked") val isBlocked: Boolean = false,
    @ColumnInfo(name = "has_meditation") val hasMeditation: Boolean = false,
    @ColumnInfo(name = "will_open_at") val willOpenAt: Long? = null,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
)