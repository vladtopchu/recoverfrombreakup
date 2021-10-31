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

    private val _uri: MutableLiveData<String> = MutableLiveData("")
    val uri = _uri.asLiveData()

    fun setPlayerUri(uri: String) {
        _uri.postValue(uri)

    }

    fun startPlayer() {
        Log.d("TESTTEST", "Player started")
        _playerCommand.postValue(MediaPlayerCommand.START)
    }

    fun pausePlayer() {
        Log.d("TESTTEST", "Player paused")
        _playerCommand.postValue(MediaPlayerCommand.PAUSE)
    }

    fun stopPlayer() {
        Log.d("TESTTEST", "Player stopped")
        _playerCommand.postValue(MediaPlayerCommand.STOP)
    }

    fun resetPlayer() {
        Log.d("TESTTEST", "Player reset")
        _playerCommand.postValue(MediaPlayerCommand.RESET)
        _uri.postValue("")
    }

    // initialized = reseted
    fun statePlayerInitialized() {
        Log.d("TESTTEST", "PSTATE inited")
        _playerState.postValue(MediaPlayerState.INITIALIZED)
    }

    fun statePlayerUriSet() {
        Log.d("TESTTEST", "PSTATE uri_Set")
        _playerState.postValue(MediaPlayerState.URI_SET)
    }

    fun statePlayerLoading() {
        Log.d("TESTTEST", "PSTATE loading")
        _playerState.postValue(MediaPlayerState.LOADING)
    }

    // ready = stopped
    fun statePlayerReady() {
        Log.d("TESTTEST", "PSTATE ready")
        _playerState.postValue(MediaPlayerState.READY)
    }

    fun statePlayerPlaying() {
        Log.d("TESTTEST", "PSTATE playing")
        _playerState.postValue(MediaPlayerState.PLAYING)
    }

    fun statePlayerPaused() {
        Log.d("TESTTEST", "PSTATE paused")
        _playerState.postValue(MediaPlayerState.PAUSED)
    }

    fun statePlayerReleased() {
        Log.d("TESTTEST", "PSTATE not_init")
        _playerState.postValue(MediaPlayerState.NOT_INITIALIZED)
    }
}