package com.topchu.recoverfrombreakup.presentation.meditations

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.topchu.recoverfrombreakup.utils.MediaPlayerCommand
import com.topchu.recoverfrombreakup.utils.MediaPlayerState
import com.topchu.recoverfrombreakup.utils.asLiveData

class SharedViewModel (
) : ViewModel() {

    private val _playerCommand: MutableLiveData<MediaPlayerCommand> = MutableLiveData(MediaPlayerCommand.NULL)
    val playerCommand = _playerCommand.asLiveData()

    private val _playerState: MutableLiveData<MediaPlayerState> = MutableLiveData(MediaPlayerState.NOT_INITIALIZED)
    val playerState = _playerState.asLiveData()

    private val _changeButtonIcon: MutableLiveData<Boolean> = MutableLiveData(false)
    val changeButtonIcon = _changeButtonIcon.asLiveData()

    private val _uriRequested: MutableLiveData<Boolean> = MutableLiveData(false)
    val uriRequested = _uriRequested.asLiveData()

    private val _uri: MutableLiveData<String> = MutableLiveData("")
    val uri = _uri.asLiveData()

    fun setUriRequested(boolean: Boolean) {
        _uriRequested.postValue(boolean)
    }

    fun setPlayerUri(uri: String) {
        _uri.postValue(uri)
    }

    fun startPlayer() {
        _playerCommand.postValue(MediaPlayerCommand.START)
    }

    fun pausePlayer() {
        _playerCommand.postValue(MediaPlayerCommand.PAUSE)
    }

    fun stopPlayer() {
        _playerCommand.postValue(MediaPlayerCommand.STOP)
    }

    fun resetPlayer() {
        _playerCommand.postValue(MediaPlayerCommand.RESET)
        _uri.postValue("")
    }

    // initialized = reseted
    fun statePlayerInitialized() {
        _playerState.postValue(MediaPlayerState.INITIALIZED)
    }

    fun statePlayerUriSet() {
        _playerState.postValue(MediaPlayerState.URI_SET)
    }

    fun statePlayerLoading() {
        _playerState.postValue(MediaPlayerState.LOADING)
    }

    // ready = stopped
    fun statePlayerReady() {
        _playerState.postValue(MediaPlayerState.READY)
    }

    fun statePlayerPlaying() {
        _playerState.postValue(MediaPlayerState.PLAYING)
    }

    fun statePlayerPaused() {
        _playerState.postValue(MediaPlayerState.PAUSED)
    }

    fun statePlayerReleased() {
        _playerState.postValue(MediaPlayerState.NOT_INITIALIZED)
    }
}