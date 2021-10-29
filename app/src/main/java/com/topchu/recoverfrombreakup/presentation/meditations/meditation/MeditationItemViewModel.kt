package com.topchu.recoverfrombreakup.presentation.meditations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.topchu.recoverfrombreakup.data.local.daos.MeditationDao
import com.topchu.recoverfrombreakup.data.local.entities.MeditationEntity
import com.topchu.recoverfrombreakup.di.ApplicationScope
import com.topchu.recoverfrombreakup.utils.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeditationItemViewModel @Inject constructor (
    private val meditationDao: MeditationDao,
    @ApplicationScope private val applicationScope: CoroutineScope,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _meditation: MutableLiveData<MeditationEntity> = MutableLiveData()
    val meditation = _meditation.asLiveData()

    private val _isPlayClicked: MutableLiveData<Boolean> = MutableLiveData(false)
    val isPlayClicked = _isPlayClicked.asLiveData()

    init {
        savedStateHandle.get<Int>("meditationId")?.let { meditationId ->
            applicationScope.launch {
                val candidate = meditationDao.getMeditation(meditationId)
                _meditation.postValue(candidate)
            }
        }
    }

    fun setIsPlayClicked(state: Boolean) {
        _isPlayClicked.postValue(state)
    }
}