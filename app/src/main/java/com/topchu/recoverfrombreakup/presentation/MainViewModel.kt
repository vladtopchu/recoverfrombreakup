package com.topchu.recoverfrombreakup.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.SkuDetails
import com.topchu.recoverfrombreakup.data.local.daos.NotificationDao
import com.topchu.recoverfrombreakup.data.local.entities.NotificationEntity
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.di.ApplicationScope
import com.topchu.recoverfrombreakup.utils.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val notificationDao: NotificationDao
) : ViewModel() {
    val _notifications = MutableLiveData<List<NotificationEntity>>(listOf())

    init {
        viewModelScope.launch {
            notificationDao.getAllNotifications().collect {
                _notifications.postValue(it)
            }
        }
    }

    fun deactivateNotification(id: Long) {
        viewModelScope.launch {
            notificationDao.deactivateNotification(id)
        }
    }
}