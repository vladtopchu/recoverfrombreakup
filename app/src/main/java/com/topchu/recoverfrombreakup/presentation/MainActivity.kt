package com.topchu.recoverfrombreakup.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.topchu.recoverfrombreakup.R
import com.topchu.recoverfrombreakup.databinding.ActivityMainBinding
import com.topchu.recoverfrombreakup.utils.Constants.TEXT_STYLE_ACTIVE
import com.topchu.recoverfrombreakup.utils.Constants.TEXT_STYLE_INACTVIVE
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

        initNavButtons()
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

//private fun initNavButtons() {
//    navButtons = mutableListOf(
//        MainActivity.NavButton(
//            true,
//            binding.bottomNav.toMeditations,
//            binding.bottomNav.toMeditationsImage,
//            binding.bottomNav.toMeditationsName,
//            R.drawable.ic_meditations_active,
//            R.drawable.ic_meditations,
//            R.id.meditationsFragment
//        ),
//        MainActivity.NavButton(
//            false,
//            binding.bottomNav.toTasks,
//            binding.bottomNav.toTasksImage,
//            binding.bottomNav.toTasksName,
//            R.drawable.ic_tasks_active,
//            R.drawable.ic_tasks,
//            R.id.tasksFragment
//        ),
//        MainActivity.NavButton(
//            false,
//            binding.bottomNav.toProfile,
//            binding.bottomNav.toProfileImage,
//            binding.bottomNav.toProfileName,
//            R.drawable.ic_profile_active,
//            R.drawable.ic_profile,
//            R.id.profileFragment
//        )
//    )
//    navButtons.forEach { btn ->
//        if(btn.isActive) {
//            btn.image.setImageResource(btn.activeIcon)
//            btn.text.setTextAppearance(TEXT_STYLE_ACTIVE)
//        } else {
//            btn.image.setImageResource(btn.inactiveIcon)
//            btn.text.setTextAppearance(TEXT_STYLE_INACTVIVE)
//        }
//        btn.view.setOnClickListener {
//            toggleFragment(btn.destination)
//        }
//    }
//}

//private fun toggleFragment(fragmentId: Int) {
//    navButtons.forEach {
//        if(it.destination == fragmentId){
//            if(!it.isActive){
//                it.isActive = true
//                it.image.setImageResource(it.activeIcon)
//                it.text.setTextAppearance(TEXT_STYLE_ACTIVE)
//                navController.navigate(it.destination, null, navOptions)
//            }
//        } else {
//            if(it.isActive){
//                it.isActive = false
//                it.image.setImageResource(it.inactiveIcon)
//                it.text.setTextAppearance(TEXT_STYLE_INACTVIVE)
//            }
//        }
//    }
//    currentFragment = fragmentId
//}

//data class NavButton(
//    var isActive: Boolean,
//    val view: View,
//    val image: ImageView,
//    val text: TextView,
//    val activeIcon: Int,
//    val inactiveIcon: Int,
//    val destination: Int
//)