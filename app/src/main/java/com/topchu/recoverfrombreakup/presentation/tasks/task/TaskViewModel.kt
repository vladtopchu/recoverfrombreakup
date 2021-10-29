package com.topchu.recoverfrombreakup.presentation.tasks.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
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
class TaskViewModel @Inject constructor (
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val _task: MutableLiveData<TaskEntity> = MutableLiveData()
    var task = _task.asLiveData()

    init {
        savedStateHandle.get<Int>("taskId")?.let { taskId ->
            applicationScope.launch {
                val candidate = taskDao.getTaskById(taskId)
                _task.postValue(candidate)
                if(candidate.isActive){
                    taskDao.makeTaskUnactive(taskId)
                    if(taskId != 21){
                        taskDao.setOpeningTimer(taskId+1, System.currentTimeMillis() + 30000)
                    }
                }
            }
        }
    }
}