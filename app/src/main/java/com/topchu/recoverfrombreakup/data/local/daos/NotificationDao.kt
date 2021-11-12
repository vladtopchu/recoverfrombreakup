package com.topchu.recoverfrombreakup.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.topchu.recoverfrombreakup.data.local.entities.MeditationEntity
import com.topchu.recoverfrombreakup.data.local.entities.NotificationEntity
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications")
    suspend fun getAllNotifications(): List<NotificationEntity>

    @Query("SELECT * FROM notifications WHERE is_active = 1 ORDER BY timestamp DESC")
    suspend fun getActiveNotifications(): List<NotificationEntity>

    @Query("SELECT * FROM notifications WHERE is_active = 0 ORDER BY timestamp DESC")
    suspend fun getInactiveNotifications(): List<NotificationEntity>

    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getNotification(id: Int): NotificationEntity

    @Query("UPDATE notifications SET is_active = 0 WHERE id = :id")
    suspend fun deactivateNotification(id: Int)

    @Insert
    suspend fun insertNotification(notification: NotificationEntity)
}