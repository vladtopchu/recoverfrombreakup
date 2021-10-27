package com.topchu.recoverfrombreakup.utils

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>
fun <T> MutableLiveData<T>.updateTasks() {
    this.value = this.value
}

fun Long.toTimeString() = SimpleDateFormat("dd:MM:yyyy:hh:mm:ss", Locale.ROOT).format(Date(this))
fun Long.toTimeObject(): TimeObject {
    val array = this.toTimeString().split(":").map { it.toInt() }
    return TimeObject(
        array.get(0),
        array.get(1),
        array.get(2),
        array.get(3),
        array.get(4),
        array.get(5)
    )
}
