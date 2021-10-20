package com.topchu.recoverfrombreakup.data.local

import androidx.room.CoroutinesRoom
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.topchu.recoverfrombreakup.data.local.daos.MeditationDao
import com.topchu.recoverfrombreakup.data.local.daos.TaskDao
import com.topchu.recoverfrombreakup.data.local.entities.MeditationEntity
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [TaskEntity::class, MeditationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun meditationDao(): MeditationDao

    class Callback @Inject constructor(
        // with Provider we can get dependencies lazily
        // it means that injected value will not be instantiated after declaration
        // but when a ".get()" method will be called
        // so now we have "onCreate" method that by itself won't be fired right after app's
        // opening and furthermore "db" value will be ready when ".get()" is called
        private val database: Provider<AppDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val taskDao = database.get().taskDao()

            applicationScope.launch {
                taskDao.insertTask(TaskEntity("Title 1", "Subtitle 1"))
                taskDao.insertTask(TaskEntity("Title 2", "Subtitle 2"))
                taskDao.insertTask(TaskEntity("Title 3", "Subtitle 3"))
                taskDao.insertTask(TaskEntity("Title 4", "Subtitle 4"))
            }
        }
    }
}