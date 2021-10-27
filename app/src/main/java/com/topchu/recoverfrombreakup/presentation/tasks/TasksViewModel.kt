package com.topchu.recoverfrombreakup.presentation.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.topchu.recoverfrombreakup.data.local.daos.TaskDao
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.di.ApplicationScope
import com.topchu.recoverfrombreakup.utils.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor (
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {
    private val _tasks: MutableLiveData<List<TaskEntity>> = MutableLiveData(listOf())
    val tasks =  _tasks.asLiveData()

    init {
        applicationScope.launch {
            _tasks.postValue(taskDao.getAllTasks())
        }
    }

    fun updateTasks() {
        applicationScope.launch {
            _tasks.postValue(taskDao.getAllTasks())
        }
    }
}