package com.topchu.recoverfrombreakup.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.data.local.AppDatabase
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var auth: FirebaseAuth

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = AppDatabase.getInstance(this)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavView.setupWithNavController(navController)

        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                for(task: TaskEntity in database.taskDao.getAllTasks()){
                    Log.d("RFB_TEST", task.toString())
                }
            }
        }
    }
}

//        binding.signOut.setOnClickListener {
//            auth.signOut()
//            val intent = Intent(this, StartActivity::class.java)
//            startActivity(intent)
//            finish()
//        }