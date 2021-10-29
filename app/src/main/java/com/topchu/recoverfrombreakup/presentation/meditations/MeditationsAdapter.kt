package com.topchu.recoverfrombreakup.presentation.meditations

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.topchu.recoverfrombreakup.data.local.entities.MeditationEntity
import com.topchu.recoverfrombreakup.presentation.meditations.meditation.MeditationFragment

class MeditationsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    lateinit var meditationsList: List<MeditationEntity>

    override fun getItemCount(): Int = meditationsList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = MeditationFragment()
        fragment.arguments = Bundle().apply {
            putInt("meditationId", position + 1)
        }
        return fragment
    }
}