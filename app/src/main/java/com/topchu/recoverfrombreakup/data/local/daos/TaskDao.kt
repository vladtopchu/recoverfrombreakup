package com.topchu.recoverfrombreakup.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity

    @Query("UPDATE tasks SET is_opened = 1, is_active = 1, will_open_at = null WHERE id = :id")
    suspend fun openTask(id: Int)

    @Query("UPDATE tasks SET is_opened = 0 WHERE id >= :id AND is_opened = 1")
    suspend fun closeTasksFrom(id: Int)

    @Query("UPDATE tasks SET is_opened = 1 WHERE id <= :id AND is_opened = 0")
    suspend fun openTasksUpTo(id: Int)

    @Query("UPDATE tasks SET is_active = 1 WHERE id = :id")
    suspend fun makeTaskActive(id: Int)

    @Query("UPDATE tasks SET is_active = 0 WHERE id = :id")
    suspend fun makeTaskInactive(id: Int)

    @Query("UPDATE tasks SET will_open_at = :timestamp  WHERE id = :id")
    suspend fun setOpeningTimer(id: Int, timestamp: Long)

    @Query("UPDATE tasks SET is_blocked = 0 WHERE is_blocked = 1")
    suspend fun unlockTasks()

    @Query("UPDATE tasks SET is_blocked = 1 WHERE id > 5")
    suspend fun lockTasks()

    @Insert
    suspend fun insertTask(task: TaskEntity)
}