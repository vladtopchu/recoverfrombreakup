package com.topchu.recoverfrombreakup.presentation.meditations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.topchu.recoverfrombreakup.utils.MediaPlayerCommand
import com.topchu.recoverfrombreakup.utils.MediaPlayerState
import com.topchu.recoverfrombreakup.utils.asLiveData

class SharedViewModel (
) : ViewModel() {

    private val _playerCommand: MutableLiveData<MediaPlayerCommand> = MutableLiveData(MediaPlayerCommand.NOT_INITIALIZED)
    val playerCommand = _playerCommand.asLiveData()

    private val _playerState: MutableLiveData<MediaPlayerState> = MutableLiveData(MediaPlayerState.IDLE)
    val playerState = _playerState.asLiveData()

    private val _uri: MutableLiveData<String> = MutableLiveData()
    val uri = _uri.asLiveData()

    fun setPlayerUri(uri: String) {
        _uri.postValue(uri)
    }

    fun clearPlayerUri() {
        _uri.postValue(null)
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
    }

    fun statePlayerLoading() {
        _playerState.postValue(MediaPlayerState.LOADING)
    }

    fun statePlayerPlaying() {
        _playerState.postValue(MediaPlayerState.PLAYING)
    }

    fun statePlayerReleased() {
        _playerState.postValue(MediaPlayerState.IDLE)
    }

    fun statePlayerPaused() {
        _playerState.postValue(MediaPlayerState.PAUSED)
    }
}