package com.topchu.recoverfrombreakup.presentation.meditations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.topchu.recoverfrombreakup.data.local.daos.MeditationDao
import com.topchu.recoverfrombreakup.data.local.daos.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MeditationsViewModel @Inject constructor (
    private val meditationDao: MeditationDao
) : ViewModel() {
    val meditations = meditationDao.getAllMeditations().asLiveData()
}