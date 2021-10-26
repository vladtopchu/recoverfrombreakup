package com.topchu.recoverfrombreakup.utils

import android.content.Context
import android.content.SharedPreferences
import com.topchu.recoverfrombreakup.utils.Constants.FILENAME_KEY
import com.topchu.recoverfrombreakup.utils.Constants.FIRST_LAUNCH_KEY
import com.topchu.recoverfrombreakup.utils.Constants.HINT_HIDE
import com.topchu.recoverfrombreakup.utils.Constants.LAST_OPENED_TASK
import com.topchu.recoverfrombreakup.utils.Constants.TOKEN_KEY

class SharedPref(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(FILENAME_KEY, Context.MODE_PRIVATE)

    fun isFirstLaunch(): Boolean = sharedPreferences.getBoolean(FIRST_LAUNCH_KEY, true)

    fun setNotFirstLaunch() {
        sharedPreferences.edit().putBoolean(FIRST_LAUNCH_KEY, false).apply()
    }

    fun isHintHided() = sharedPreferences.getBoolean(HINT_HIDE, false)

    fun hideHint() {
        sharedPreferences.edit().putBoolean(HINT_HIDE, true).apply()
    }

    fun getLastOpenedTask(): Int = sharedPreferences.getInt(LAST_OPENED_TASK, 0)

    fun setLastOpenedTask(id: Int) {
        sharedPreferences.edit().putInt(LAST_OPENED_TASK, id).apply()
    }

    fun wipeUserToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }

    fun setUserToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getUserToken(): String? = sharedPreferences.getString(TOKEN_KEY, null)
}
