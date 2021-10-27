package com.topchu.recoverfrombreakup.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.topchu.recoverfrombreakup.R
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

    @Inject
    lateinit var navOptions: NavOptions

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var currentFragment = R.id.tasksFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(sharedPref.isFirstLaunch()){
            val intent = Intent(this, StartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_main)
        navController.navigate(R.id.tasksFragment)



        if(!sharedPref.isContentBought()){
            binding.buyContent.visibility = View.VISIBLE
            binding.buyContent.setOnClickListener {
                navController.navigate(R.id.buyFragment)
            }
        }

        binding.bottomNav.toTasks.setOnClickListener {
            toggleFragment(R.id.tasksFragment)
        }

        binding.bottomNav.toMeditations.setOnClickListener {
            toggleFragment(R.id.meditationsFragment)
        }
    }

    private fun toggleFragment(fragmentId: Int) {
        if(currentFragment != fragmentId){
            when(fragmentId){
                R.id.tasksFragment -> {
                    binding.bottomNav.toTasksImage.setImageResource(R.drawable.ic_tasks_active)
                    binding.bottomNav.toTasksName.setTextAppearance(R.style.bottomNavTextActive)
                    binding.bottomNav.toMeditationsImage.setImageResource(R.drawable.ic_meditations)
                    binding.bottomNav.toMeditationsName.setTextAppearance(R.style.bottomNavTextInactive)
                    currentFragment = fragmentId
                    navController.navigate(fragmentId, null, navOptions)
                }
                R.id.meditationsFragment -> {
                    binding.bottomNav.toMeditationsImage.setImageResource(R.drawable.ic_meditations_active)
                    binding.bottomNav.toMeditationsName.setTextAppearance(R.style.bottomNavTextActive)
                    binding.bottomNav.toTasksImage.setImageResource(R.drawable.ic_tasks)
                    binding.bottomNav.toTasksName.setTextAppearance(R.style.bottomNavTextInactive)
                    currentFragment = fragmentId
                    navController.navigate(fragmentId, null, navOptions)
                }
            }
        }
    }

    fun hideBuyButton() {
        binding.buyContent.visibility = View.GONE
    }
}

//        binding.signOut.setOnClickListener {
//            auth.signOut()
//            val intent = Intent(this, StartActivity::class.java)
//            startActivity(intent)
//            finish()
//        }