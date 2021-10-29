package com.topchu.recoverfrombreakup.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
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

    var currentFragment = R.id.tasksFragment

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

        if(!sharedPref.isContentBought()){
            binding.buyContent.visibility = View.VISIBLE
            binding.buyContent.setOnClickListener {
                navController.navigate(R.id.buyFragment, null, navOptions)
            }
        }

        activateTasksButton()

        binding.bottomNav.toTasks.setOnClickListener {
            toggleFragment(R.id.tasksFragment)
        }

        binding.bottomNav.toMeditations.setOnClickListener {
            toggleFragment(R.id.meditationsFragment)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(navController.currentDestination?.id != currentFragment) {
            when(navController.currentDestination?.id){
                R.id.tasksFragment -> {
                    activateTasksButton()
                    deactivateMeditationsButton()
                }
                R.id.meditationsFragment -> {
                    activateMeditationsButton()
                    deactivateTasksButton()
                }
            }
            currentFragment = navController.currentDestination?.id!!
        }
    }

    private fun toggleFragment(fragmentId: Int) {
        if(currentFragment != fragmentId){
            when(fragmentId){
                R.id.tasksFragment -> {
                    activateTasksButton()
                    deactivateMeditationsButton()
                }
                R.id.meditationsFragment -> {
                    activateMeditationsButton()
                    deactivateTasksButton()
                }
            }
            navController.popBackStack(currentFragment, true)
            currentFragment = fragmentId
            navController.navigate(fragmentId, null, navOptions)
        }
    }

    fun activateTasksButton() {
        binding.bottomNav.toTasksImage.setImageResource(R.drawable.ic_tasks_active)
        binding.bottomNav.toTasksName.setTextAppearance(R.style.bottomNavTextActive)
    }

    fun deactivateTasksButton() {
        binding.bottomNav.toTasksImage.setImageResource(R.drawable.ic_tasks)
        binding.bottomNav.toTasksName.setTextAppearance(R.style.bottomNavTextInactive)
    }

    fun activateMeditationsButton() {
        binding.bottomNav.toMeditationsImage.setImageResource(R.drawable.ic_meditations_active)
        binding.bottomNav.toMeditationsName.setTextAppearance(R.style.bottomNavTextActive)
    }

    fun deactivateMeditationsButton() {
        binding.bottomNav.toMeditationsImage.setImageResource(R.drawable.ic_meditations)
        binding.bottomNav.toMeditationsName.setTextAppearance(R.style.bottomNavTextInactive)
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