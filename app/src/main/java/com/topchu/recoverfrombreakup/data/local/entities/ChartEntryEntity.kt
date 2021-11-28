package com.topchu.recoverfrombreakup.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chart_entries")
data class ChartEntryEntity(
    val date: String,
    val value: Int,
    val createdAt: Long,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
)