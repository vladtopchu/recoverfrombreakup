package com.topchu.recoverfrombreakup.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.topchu.recoverfrombreakup.data.local.daos.MeditationDao
import com.topchu.recoverfrombreakup.data.local.daos.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor (
    private val taskDao: TaskDao
) : ViewModel() {
    val tasks = taskDao.getAllTasks().asLiveData()
}