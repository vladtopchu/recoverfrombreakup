package com.topchu.recoverfrombreakup.data.local

import androidx.room.CoroutinesRoom
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.data.local.daos.MeditationDao
import com.topchu.recoverfrombreakup.data.local.daos.NotificationDao
import com.topchu.recoverfrombreakup.data.local.daos.TaskDao
import com.topchu.recoverfrombreakup.data.local.entities.MeditationEntity
import com.topchu.recoverfrombreakup.data.local.entities.NotificationEntity
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.di.ApplicationScope
import com.topchu.recoverfrombreakup.utils.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [TaskEntity::class, MeditationEntity::class, NotificationEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun meditationDao(): MeditationDao
    abstract fun notificationDao(): NotificationDao

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
            val meditationDao = database.get().meditationDao()
            val notificationDao = database.get().notificationDao()

            applicationScope.launch {
                taskDao.insertTask(TaskEntity("День 1", "Сжигаем мосты",
                    listOf(R.string.a_a, R.string.a_b, R.string.a_c, R.string.a_d, R.string.a_e, R.string.a_f, R.string.a_g),
                    true, true, false, true, 1))

                taskDao.insertTask(TaskEntity("День 2", "Убираем тревожность",
                    listOf(R.string.b_a, R.string.b_b, R.string.b_c, R.string.b_d),
                    false, false, false, true, 2))

                taskDao.insertTask(TaskEntity("День 3", "Стираем «идеальность»",
                    listOf(R.string.c_a, R.string.c_b, R.string.c_c, R.string.c_d),
                    false, false, false))

                taskDao.insertTask(TaskEntity("День 4", "Правила и ограничения",
                    listOf(R.string.d_a, R.string.d_b, R.string.d_c, R.string.d_d, R.string.d_e, R.string.d_f, R.string.d_g),
                    false, false, false))

                taskDao.insertTask(TaskEntity("День 5", "Физическая нагрузка",
                    listOf(R.string.e_a, R.string.e_b, R.string.e_c, R.string.e_d, R.string.e_e, R.string.e_f),
                    false, false, false))

                taskDao.insertTask(TaskEntity("День 6", "Ставим точку",
                    listOf(R.string.f_a, R.string.f_b, R.string.f_c, R.string.f_d),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 7", "Работа со страхами",
                    listOf(R.string.g_a, R.string.g_b, R.string.g_c),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 8", "Позитивные эмоции",
                    listOf(R.string.h_a, R.string.h_b, R.string.h_c, R.string.h_d, R.string.h_e),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 9", "Компенсация общения",
                    listOf(R.string.i_a, R.string.i_b, R.string.i_c, R.string.i_d),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 10", "Избавляемся от лишнего",
                    listOf(R.string.j_a, R.string.j_b, R.string.j_c, R.string.j_d),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 11", "Вовлеченность в свою жизнь",
                    listOf(R.string.k_a, R.string.k_b, R.string.k_c, R.string.k_d, R.string.k_e),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 12", "Планирование",
                    listOf(R.string.l_a, R.string.l_b, R.string.l_c, R.string.l_d, R.string.l_e, R.string.l_f),
                    false, false, true, true, 3))

                taskDao.insertTask(TaskEntity("День 13", "Анализ эмоций",
                    listOf(R.string.m_a, R.string.m_b, R.string.m_c),
                    false, false, true, true, 4))

                taskDao.insertTask(TaskEntity("День 14", "Воспоминания и мечты",
                    listOf(R.string.n_a, R.string.n_b, R.string.n_c,  R.string.n_d),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 15", "Солнце внутри",
                    listOf(R.string.o_a, R.string.o_b, R.string.o_c,  R.string.o_d),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 16", "Границы и норма в отношениях",
                    listOf(R.string.p_a, R.string.p_b, R.string.p_c,  R.string.p_d),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 17", "Работа с привлечением внимания",
                    listOf(R.string.q_a, R.string.q_b, R.string.q_c,  R.string.q_d),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 18", "Женственность",
                    listOf(R.string.r_a, R.string.r_b, R.string.r_c,  R.string.r_d),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 19", "Работа с личными качествами",
                    listOf(R.string.s_a, R.string.s_b, R.string.s_c,  R.string.s_d, R.string.s_e, R.string.s_f, R.string.s_g,  R.string.s_h, R.string.s_i, R.string.s_j, R.string.s_k,  R.string.s_l),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 20", "Гайд по свиданиям",
                    listOf(R.string.t_a, R.string.t_b, R.string.t_c,  R.string.t_d, R.string.t_e, R.string.t_f, R.string.t_g,  R.string.t_h, R.string.t_i, R.string.t_j, R.string.t_k,  R.string.t_l, R.string.t_m, R.string.t_n, R.string.t_o,  R.string.t_p, R.string.t_q, R.string.t_r, R.string.t_s,  R.string.t_t),
                    false, false, true))

                taskDao.insertTask(TaskEntity("День 21", "Благодарность",
                    listOf(R.string.u_a, R.string.u_b, R.string.u_c,  R.string.u_d, R.string.u_e),
                    false, false, true))

                meditationDao.insertMeditation(MeditationEntity(
                    "Очищение водой\n(День 1)",
                    "https://firebasestorage.googleapis.com/v0/b/recoveryapp-eae12.appspot.com/o/meditation_a.mp3?alt=media&token=d69d10af-b4f7-43b8-b833-3df6f336e940",
                    1, true, false))

                meditationDao.insertMeditation(MeditationEntity("Снятие утренней тревожности\n(День 2)",
                    "https://firebasestorage.googleapis.com/v0/b/recoveryapp-eae12.appspot.com/o/meditation_b.mp3?alt=media&token=5072dbd2-a648-4af6-bdbb-b617d46eb0cc",
                    2, false, false))

                meditationDao.insertMeditation(MeditationEntity("Привлечение деятельной энергии\n(День 12)",
                    "https://firebasestorage.googleapis.com/v0/b/recoveryapp-eae12.appspot.com/o/meditation_c.mp3?alt=media&token=a7920eaf-87fd-4346-b3b4-6af35468450e",
                    12, false))

                meditationDao.insertMeditation(MeditationEntity("Снятие внезапных приступов паники и ощущения потери (День 13)",
                    "https://firebasestorage.googleapis.com/v0/b/recoveryapp-eae12.appspot.com/o/meditation_d.mp3?alt=media&token=83f298bb-c036-4496-b0c7-005f31eac0c8",
                    13, false))

                notificationDao.insertNotification(NotificationEntity(
                    "Подсказка",
                    "Новое задание откроется через 24 часа после прохождения предыдущего!",
                    System.currentTimeMillis()))
                notificationDao.insertNotification(NotificationEntity(
                    "Важно!",
                    "Без авторизации Ваш прогресс может быть утерян при переустановке приложения, а так же Вы не сможете приобрести платный контент и перенести текущий прогресс на другие устройства.",
                    System.currentTimeMillis() + 1000))
            }
        }
    }
}