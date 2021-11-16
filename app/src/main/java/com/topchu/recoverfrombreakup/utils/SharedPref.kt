package com.topchu.recoverfrombreakup.utils

import android.content.Context
import android.content.SharedPreferences
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_FILENAME
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_FIRST_LAUNCH
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_FIRST_STORE_VISIT
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_IS_HINT_HIDED
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_IS_CONTENT_BOUGHT
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_LOCAL_PROGRESS
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_TOKEN
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_USER_PROGRESS

class SharedPref(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SPKEY_FILENAME, Context.MODE_PRIVATE)

    fun isFirstLaunch(): Boolean = sharedPreferences.getBoolean(SPKEY_FIRST_LAUNCH, true)

    fun setNotFirstLaunch() {
        sharedPreferences.edit().putBoolean(SPKEY_FIRST_LAUNCH, false).apply()
    }

    fun setLocalProgress(id: Int) {
        sharedPreferences.edit().putInt(SPKEY_LOCAL_PROGRESS, id).apply()
    }

    fun getLocalProgress() = sharedPreferences.getInt(SPKEY_LOCAL_PROGRESS, 1)

    fun setUserProgress(id: Int) {
        sharedPreferences.edit().putInt(SPKEY_USER_PROGRESS, id).apply()
    }

    fun getUserProgress() = sharedPreferences.getInt(SPKEY_USER_PROGRESS, 1)

    fun isContentBought() = sharedPreferences.getBoolean(SPKEY_IS_CONTENT_BOUGHT, false)

    fun setContentBought(bool: Boolean) {
        sharedPreferences.edit().putBoolean(SPKEY_IS_CONTENT_BOUGHT, bool).apply()
    }

    fun setNotFirstStoreVisit() {
        sharedPreferences.edit().putBoolean(SPKEY_FIRST_STORE_VISIT, false).apply()
    }

    fun isFirstStoreVisit(): Boolean = sharedPreferences.getBoolean(SPKEY_FIRST_STORE_VISIT, true)
}
