package com.topchu.recoverfrombreakup.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity

    @Query("UPDATE tasks SET is_opened = 1 WHERE id = :id")
    suspend fun openTaskById(id: Int)

    @Query("UPDATE tasks SET is_blocked = 0")
    suspend fun unlockTasks()

    @Insert
    suspend fun insertTask(task: TaskEntity)
}