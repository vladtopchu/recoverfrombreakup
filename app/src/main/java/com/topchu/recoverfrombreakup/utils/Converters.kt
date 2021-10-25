package com.topchu.recoverfrombreakup.utils

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Integer.parseInt
import java.lang.reflect.Type

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