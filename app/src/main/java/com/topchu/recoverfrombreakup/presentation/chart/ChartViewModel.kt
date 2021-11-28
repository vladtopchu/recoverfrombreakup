package com.topchu.recoverfrombreakup.presentation.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topchu.recoverfrombreakup.data.local.daos.ChartDao
import com.topchu.recoverfrombreakup.data.local.entities.ChartEntryEntity
import com.topchu.recoverfrombreakup.utils.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor (
    private val chartDao: ChartDao
) : ViewModel() {

    private val _entries: MutableLiveData<List<ChartEntryEntity>> = MutableLiveData(null)
    val entries =  _entries.asLiveData()

    init {
        viewModelScope.launch {
            chartDao.getChartEntries().collect {
                _entries.postValue(it)
            }
        }
    }

}