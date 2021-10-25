package com.topchu.recoverfrombreakup.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.data.local.AppDatabase
import com.topchu.recoverfrombreakup.databinding.ActivityMainBinding
import com.topchu.recoverfrombreakup.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var sharedPref: SharedPref

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(sharedPref.isFirstLaunch()){
            val intent = Intent(this, StartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_main)
        binding.bottomNavView.setupWithNavController(navController)

    }
}

//        binding.signOut.setOnClickListener {
//            auth.signOut()
//            val intent = Intent(this, StartActivity::class.java)
//            startActivity(intent)
//            finish()
//        }