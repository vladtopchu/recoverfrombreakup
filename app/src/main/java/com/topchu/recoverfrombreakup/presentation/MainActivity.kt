package com.topchu.recoverfrombreakup.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

    private lateinit var navButtons: List<NavButton>

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
        navController.currentDestination?.id?.let {
            toggleButton(it)
        }
    }

    fun toggleButton(fragmentId: Int) {
        for(button: NavButton in navButtons) {
            if(button.ids.contains(fragmentId)) {
                button.activate()
            } else {
                button.deactivate()
            }
        }
    }

    private fun initNavButtons() {
        navButtons = listOf(
            NavButton(
                listOf(R.id.tasksFragment, R.id.taskFragment),
                binding.bottomNav.toTasksImage,
                binding.bottomNav.toTasksName,
                R.drawable.ic_tasks_active,
                R.drawable.ic_tasks,
                isActive = true
            ),
            NavButton(
                listOf(R.id.meditationsFragment, R.id.meditationFragment),
                binding.bottomNav.toMeditationsImage,
                binding.bottomNav.toMeditationsName,
                R.drawable.ic_meditations_active,
                R.drawable.ic_meditations
            ),
            NavButton(
                listOf(R.id.chartFragment),
                binding.bottomNav.toChartImage,
                binding.bottomNav.toChartName,
                R.drawable.ic_chart_active,
                R.drawable.ic_chart
            ),
            NavButton(
                listOf(R.id.profileFragment),
                binding.bottomNav.toProfileImage,
                binding.bottomNav.toProfileName,
                R.drawable.ic_profile_active,
                R.drawable.ic_profile
            )
        )

        binding.bottomNav.toTasks.setOnClickListener {
            if(navController.currentDestination?.id != R.id.tasksFragment){
                toggleButton(R.id.tasksFragment)
                navController.navigate(R.id.tasksFragment, null, navOptions)
            }
        }

        binding.bottomNav.toMeditations.setOnClickListener {
            if(navController.currentDestination?.id != R.id.meditationsFragment){
                toggleButton(R.id.meditationsFragment)
                navController.navigate(R.id.meditationsFragment, null, navOptions)
            }
        }

        binding.bottomNav.toChart.setOnClickListener {
            if(navController.currentDestination?.id != R.id.chartFragment){
                toggleButton(R.id.chartFragment)
                navController.navigate(R.id.chartFragment, null, navOptions)
            }
        }

        binding.bottomNav.toProfile.setOnClickListener {
            if(navController.currentDestination?.id != R.id.profileFragment){
                toggleButton(R.id.profileFragment)
                navController.navigate(R.id.profileFragment, null, navOptions)
            }
        }
    }
}

private class NavButton(
    val ids: List<Int>,
    val imageView: ImageView,
    val textView: TextView,
    val activeImage: Int,
    val inactiveImage: Int,
    var isActive: Boolean = false
) {

    fun activate(){
        imageView.setImageResource(activeImage)
        textView.setTextAppearance(TEXT_STYLE_ACTIVE)
        isActive = true
    }
    fun deactivate(){
        imageView.setImageResource(inactiveImage)
        textView.setTextAppearance(TEXT_STYLE_INACTVIVE)
        isActive = false
    }
}