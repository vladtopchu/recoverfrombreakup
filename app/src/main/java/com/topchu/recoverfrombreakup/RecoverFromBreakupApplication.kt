package com.topchu.recoverfrombreakup

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.topchu.recoverfrombreakup.di.ApplicationScope
import com.topchu.recoverfrombreakup.presentation.MainActivity
import com.topchu.recoverfrombreakup.presentation.StartActivity
import com.topchu.recoverfrombreakup.utils.SharedPref
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class RecoverFromBreakupApplication : Application() {

    @Inject
    lateinit var firestoreUsers: CollectionReference


    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        if(sharedPref.getUserProgress() != 0) {
            if(Firebase.auth.currentUser != null) {
                applicationScope.launch {
                    withContext(Dispatchers.IO){
                        val updateData = hashMapOf("progress" to sharedPref.getUserProgress())
                        firestoreUsers.document(Firebase.auth.currentUser!!.uid)
                            .set(updateData, SetOptions.merge())
                            .addOnCompleteListener {
                                Timber.d("Application: set current progress to user remotely")
                            }
                            .addOnFailureListener {
                                Timber.d("Application: fail on update user remotely")
                            }
                    }
                }
            }
        }
    }
}