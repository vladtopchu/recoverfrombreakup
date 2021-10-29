package com.topchu.recoverfrombreakup.utils

data class TimeObject(
    val day: Int,
    val month: Int,
    val year: Int,
    val hour: Int,
    val minute: Int,
    val second: Int
) {
    override fun toString(): String = "Откроется $day/$month/$year в $hour:$minute:$second"
}