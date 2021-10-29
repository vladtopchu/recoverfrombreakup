package com.topchu.recoverfrombreakup.presentation.meditations

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.topchu.recoverfrombreakup.data.local.daos.MeditationDao
import com.topchu.recoverfrombreakup.data.local.daos.TaskDao
import com.topchu.recoverfrombreakup.data.local.entities.MeditationEntity
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.di.ApplicationScope
import com.topchu.recoverfrombreakup.utils.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.logging.Handler
import javax.inject.Inject

@HiltViewModel
class MeditationsViewModel @Inject constructor (
    private val meditationDao: MeditationDao,
    @ApplicationScope private val applicationScope: CoroutineScope,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _meditations: MutableLiveData<List<MeditationEntity>> = MutableLiveData(listOf())
    val meditations = _meditations.asLiveData()

    private val _openExactMeditation: MutableLiveData<Int> = MutableLiveData(-1)
    val openExactMeditation = _openExactMeditation.asLiveData()

    init {
        applicationScope.launch {
            val meds = meditationDao.getAllMeditations()
            _meditations.postValue(meds)
        }
    }

    fun updateMeditations() {
        applicationScope.launch {
            _meditations.postValue(meditationDao.getAllMeditations())
        }
    }
}