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
    fun getAllMeditations(): Flow<List<MeditationEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity

    @Query("UPDATE tasks SET is_opened = 1 WHERE id = :id")
    suspend fun openTaskById(id: Int)

    @Query("UPDATE tasks SET is_blocked = 0")
    suspend fun unlockTasks()

    @Insert
    suspend fun insertTask(task: TaskEntity)
}