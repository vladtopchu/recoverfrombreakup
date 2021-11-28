package com.topchu.recoverfrombreakup.utils

import androidx.room.TypeConverter
import java.lang.Integer.parseInt

class Converters {
    @TypeConverter
    fun fromList(list: List<Int>): String {
        return list.joinToString()
    }

    @TypeConverter
    fun toList(data: String): List<Int> {
        return data.split(',').map {
            it.replace(" ", "")
        }.map {
            parseInt(it)
        }
    }
}