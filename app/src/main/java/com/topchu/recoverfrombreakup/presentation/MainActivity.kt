package com.topchu.recoverfrombreakup.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.databinding.ActivityMainBinding
import com.topchu.recoverfrombreakup.utils.Constants.TEXT_STYLE_ACTIVE
import com.topchu.recoverfrombreakup.utils.Constants.TEXT_STYLE_INACTVIVE
import com.topchu.recoverfrombreakup.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var navOptions: NavOptions

    val viewModel: MainViewModel by viewModels()

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

        initNavButtons()

        viewModel._notifications.observe(this, { notifs ->
            Timber.d(notifs.toString())
            if(notifs.isNotEmpty()){
                if(notifs.any { it.isActive }) {
                    binding.bottomNav.notifIndicator.visibility = View.VISIBLE
                } else {
                    binding.bottomNav.notifIndicator.visibility = View.GONE
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(navController.currentDestination?.id != currentFragment) {
            navController.currentDestination?.id?.let {
                toggleButton(it)
            }
            currentFragment = navController.currentDestination?.id!!
        }
    }

    fun toggleButton(fragmentId: Int) {
        when(fragmentId){
            R.id.tasksFragment, R.id.taskFragment -> {
                binding.bottomNav.toTasksImage.setImageResource(R.drawable.ic_tasks_active)
                binding.bottomNav.toTasksName.setTextAppearance(TEXT_STYLE_ACTIVE)

                binding.bottomNav.toMeditationsImage.setImageResource(R.drawable.ic_meditations)
                binding.bottomNav.toMeditationsName.setTextAppearance(TEXT_STYLE_INACTVIVE)
                binding.bottomNav.toProfileImage.setImageResource(R.drawable.ic_profile)
                binding.bottomNav.toProfileName.setTextAppearance(TEXT_STYLE_INACTVIVE)
            }
            R.id.meditationsFragment, R.id.meditationFragment -> {
                binding.bottomNav.toMeditationsImage.setImageResource(R.drawable.ic_meditations_active)
                binding.bottomNav.toMeditationsName.setTextAppearance(TEXT_STYLE_ACTIVE)

                binding.bottomNav.toTasksImage.setImageResource(R.drawable.ic_tasks)
                binding.bottomNav.toTasksName.setTextAppearance(TEXT_STYLE_INACTVIVE)
                binding.bottomNav.toProfileImage.setImageResource(R.drawable.ic_profile)
                binding.bottomNav.toProfileName.setTextAppearance(TEXT_STYLE_INACTVIVE)
            }
            R.id.profileFragment -> {
                binding.bottomNav.toProfileImage.setImageResource(R.drawable.ic_profile_active)
                binding.bottomNav.toProfileName.setTextAppearance(TEXT_STYLE_ACTIVE)

                binding.bottomNav.toTasksImage.setImageResource(R.drawable.ic_tasks)
                binding.bottomNav.toTasksName.setTextAppearance(TEXT_STYLE_INACTVIVE)
                binding.bottomNav.toMeditationsImage.setImageResource(R.drawable.ic_meditations)
                binding.bottomNav.toMeditationsName.setTextAppearance(TEXT_STYLE_INACTVIVE)
            }
        }
    }

    private fun initNavButtons() {
        binding.bottomNav.toTasks.setOnClickListener {
            if(currentFragment != R.id.tasksFragment){
                toggleButton(R.id.tasksFragment)
                navController.navigate(R.id.tasksFragment, null, navOptions)
                currentFragment = R.id.tasksFragment
            }
        }

        binding.bottomNav.toMeditations.setOnClickListener {
            if(currentFragment != R.id.meditationsFragment){
                toggleButton(R.id.meditationsFragment)
                navController.navigate(R.id.meditationsFragment, null, navOptions)
                currentFragment = R.id.meditationsFragment
            }
        }

        binding.bottomNav.toProfile.setOnClickListener {
            if(currentFragment != R.id.profileFragment){
                toggleButton(R.id.profileFragment)
                navController.navigate(R.id.profileFragment, null, navOptions)
                currentFragment = R.id.profileFragment
            }
        }
    }
}