package com.topchu.recoverfrombreakup.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.topchu.recoverfrombreakup.data.local.entities.ChartEntryEntity
import com.topchu.recoverfrombreakup.data.local.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChartDao {
    @Query("SELECT * FROM chart_entries ORDER BY createdAt ASC")
    fun getChartEntries(): Flow<List<ChartEntryEntity>>

    @Query("DELETE FROM chart_entries")
    fun removeAllChartEntries()

    @Insert
    suspend fun insertChartEntry(chartEntry: ChartEntryEntity)
}