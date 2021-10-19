package com.topchu.recoverfrombreakup.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo() val title: String,
    @ColumnInfo() val subtitle: String,
    @ColumnInfo(name = "is_opened") val isOpened: Boolean,
    @ColumnInfo(name = "will_open_at") val willOpenAt: Long? = null,
    @ColumnInfo(name = "is_blocked") val isBlocked: Boolean,
    @ColumnInfo(name = "has_meditation") val hasMeditation: Boolean
)