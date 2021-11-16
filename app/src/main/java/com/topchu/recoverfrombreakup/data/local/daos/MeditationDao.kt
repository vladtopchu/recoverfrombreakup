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

    @Query("UPDATE meditations SET is_opened = 1 WHERE task_id <= :taskId AND is_opened = 0")
    suspend fun openMeditationsUpTo(taskId: Int)

    @Query("UPDATE meditations SET is_opened = 0 WHERE id >= :id AND is_opened = 1")
    suspend fun closeMeditationsFrom(id: Int)

    @Query("UPDATE meditations SET is_blocked = 0 WHERE is_blocked = 1")
    suspend fun unlockMeditations()

    @Query("UPDATE meditations SET is_blocked = 1 WHERE task_id > 5")
    suspend fun lockMeditations()

    @Insert
    suspend fun insertMeditation(meditation: MeditationEntity)
}