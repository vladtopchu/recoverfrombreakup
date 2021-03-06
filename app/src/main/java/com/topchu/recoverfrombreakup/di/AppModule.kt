package com.topchu.recoverfrombreakup.di

import android.app.Application
import android.content.Context
import androidx.navigation.NavOptions
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.data.local.AppDatabase
import com.topchu.recoverfrombreakup.utils.Constants.FIREBASE_USERS_COLLECTION
import com.topchu.recoverfrombreakup.utils.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // SupervisorJob() is needed to prevent a cascade cancellation of
    // child functions if one of 'em fails
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context)
            = SharedPref(context)

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: AppDatabase.Callback
    ) = Room.databaseBuilder(app, AppDatabase::class.java, "app_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideTaskDao(db: AppDatabase) = db.taskDao()

    @Provides
    fun provideMeditationDao(db: AppDatabase) = db.meditationDao()

    @Provides
    fun provideNotificationDao(db: AppDatabase) = db.notificationDao()

    @Provides
    fun provideChartDao(db: AppDatabase) = db.chartDao()

    @Singleton
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun provideNavOptions() = NavOptions.Builder().apply {
        setEnterAnim(R.anim.nav_default_enter_anim)
        setExitAnim(R.anim.nav_default_exit_anim)
        setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
        setPopExitAnim(R.anim.nav_default_pop_exit_anim)
    }.build()

    @Provides
    @Singleton
    fun provideFirestoreUsers() = FirebaseFirestore.getInstance()
        .collection(FIREBASE_USERS_COLLECTION)

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope