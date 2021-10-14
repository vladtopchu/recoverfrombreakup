package com.topchu.recoverfrombreakup

import android.app.Application
import android.content.Intent
import com.topchu.recoverfrombreakup.ui.HelloActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecoverFromBreakupApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val intent = Intent(this, HelloActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}