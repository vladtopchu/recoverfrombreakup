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
import com.topchu.recoverfrombreakup.utils.MediaPlayerState
import com.topchu.recoverfrombreakup.utils.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ParentViewModel (
) : ViewModel() {

    private val _flag: MutableLiveData<MediaPlayerState> = MutableLiveData(MediaPlayerState.NOT_INITIALIZED)
    val flag = _flag.asLiveData()

    private val _uri: MutableLiveData<String> = MutableLiveData()
    val uri = _uri.asLiveData()

    fun setUri(uri: String) {
        _uri.postValue(uri)
    }

    fun stop() {
        _flag.postValue(MediaPlayerState.STOP)
    }

    fun reset() {
        _flag.postValue(MediaPlayerState.RESET)
    }

    fun release() {
        _flag.postValue(MediaPlayerState.RELEASE)
    }
}