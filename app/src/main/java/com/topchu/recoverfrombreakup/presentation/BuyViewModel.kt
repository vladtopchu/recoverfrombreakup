package com.topchu.recoverfrombreakup.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.SkuDetails
import com.topchu.recoverfrombreakup.data.local.entities.TaskEntity
import com.topchu.recoverfrombreakup.utils.asLiveData
import kotlinx.coroutines.launch

class BuyViewModel: ViewModel() {
    private val _details: MutableLiveData<SkuDetails> = MutableLiveData()
    val details =  _details.asLiveData()

    fun setDetails(skuDetails: SkuDetails){
        _details.postValue(skuDetails)
    }
}