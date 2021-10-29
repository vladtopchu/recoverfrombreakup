package com.topchu.recoverfrombreakup.presentation.meditations

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
import javax.inject.Inject

@HiltViewModel
class MeditationViewModel @Inject constructor (
    private val meditationDao: MeditationDao,
    @ApplicationScope private val applicationScope: CoroutineScope,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _meditation: MutableLiveData<MeditationEntity> = MutableLiveData()
    val meditation = _meditation.asLiveData()

    init {
        savedStateHandle.get<Int>("meditationId")?.let { meditationId ->
            applicationScope.launch {
                val candidate = meditationDao.getMeditation(meditationId)
                _meditation.postValue(candidate)
            }
        }
    }

}