package com.topchu.recoverfrombreakup.presentation.chart.rate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.SkuDetails
import com.topchu.recoverfrombreakup.data.local.daos.ChartDao
import com.topchu.recoverfrombreakup.data.local.entities.ChartEntryEntity
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.di.ApplicationScope
import com.topchu.recoverfrombreakup.utils.asLiveData
import com.topchu.recoverfrombreakup.utils.toTimeString
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateViewModel @Inject constructor(
    private val chartDao: ChartDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {
    private val _rateValue: MutableLiveData<Int> = MutableLiveData(null)
    val rateValue =  _rateValue.asLiveData()

    fun setRateValue(value: Int) {
        applicationScope.launch {
            val currentTime = System.currentTimeMillis()
            chartDao.insertChartEntry(ChartEntryEntity(currentTime.toTimeString("dd.MM"), value, currentTime))
            _rateValue.postValue(value)
        }
    }
}