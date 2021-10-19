package com.topchu.recoverfrombreakup.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.topchu.recoverfrombreakup.data.local.entities.MeditationEntity
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity

@Dao
interface MeditationDao {
    @Query("SELECT * FROM meditations")
    fun getAllMeditations(): LiveData<List<MeditationEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): TaskEntity

    @Query("UPDATE tasks SET is_opened = 1 WHERE id = :id")
    fun openTaskById(id: Int)

    @Query("UPDATE tasks SET is_blocked = 0")
    fun unlockTasks()

    @Insert
    fun insertTask(task: TaskEntity)
}