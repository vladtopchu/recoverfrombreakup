package com.topchu.recoverfrombreakup.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.topchu.recoverfrombreakup.data.local.entities.MeditationEntity
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeditationDao {
    @Query("SELECT * FROM meditations")
    suspend fun getAllMeditations(): List<MeditationEntity>

    @Query("SELECT * FROM meditations WHERE id = :id")
    suspend fun getMeditation(id: Int): MeditationEntity

    @Query("UPDATE meditations SET is_opened = 1 WHERE id = :id")
    suspend fun openMeditationById(id: Int)

    @Query("UPDATE meditations SET is_blocked = 0")
    suspend fun unlockMeditations()

    @Insert
    suspend fun insertMeditation(meditation: MeditationEntity)
}