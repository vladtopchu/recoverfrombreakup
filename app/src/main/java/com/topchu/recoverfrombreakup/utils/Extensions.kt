package com.topchu.recoverfrombreakup.utils

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>

fun Long.toTimeString() = SimpleDateFormat("dd:MM:yyyy:hh:mm:ss", Locale.ROOT).format(Date(this))
fun Long.toTimeObject(): TimeObject {
    val array = this.toTimeString().split(":").map { it.toInt() }
    return TimeObject(
        array[0],
        array[1],
        array[2],
        array[3],
        array[4],
        array[5]
    )
}
