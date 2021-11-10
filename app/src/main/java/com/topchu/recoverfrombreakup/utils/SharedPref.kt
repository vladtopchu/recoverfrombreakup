package com.topchu.recoverfrombreakup.utils

import android.content.Context
import android.content.SharedPreferences
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_FILENAME
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_FIRST_LAUNCH
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_IS_HINT_HIDED
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_IS_CONTENT_BOUGHT
import com.topchu.recoverfrombreakup.utils.Constants.SPKEY_TOKEN

class SharedPref(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SPKEY_FILENAME, Context.MODE_PRIVATE)

    fun isFirstLaunch(): Boolean = sharedPreferences.getBoolean(SPKEY_FIRST_LAUNCH, true)

    fun setNotFirstLaunch() {
        sharedPreferences.edit().putBoolean(SPKEY_FIRST_LAUNCH, false).apply()
    }

    fun isHintHided() = sharedPreferences.getBoolean(SPKEY_IS_HINT_HIDED, false)

    fun hideHint() {
        sharedPreferences.edit().putBoolean(SPKEY_IS_HINT_HIDED, true).apply()
    }

    fun isContentBought() = sharedPreferences.getBoolean(SPKEY_IS_CONTENT_BOUGHT, false)

    fun setContentBought() {
        sharedPreferences.edit().putBoolean(SPKEY_IS_CONTENT_BOUGHT, true).apply()
    }

    fun wipeUserToken() {
        sharedPreferences.edit().remove(SPKEY_TOKEN).apply()
    }

    fun setUserToken(token: String) {
        sharedPreferences.edit().putString(SPKEY_TOKEN, token).apply()
    }

    fun getUserToken(): String? = sharedPreferences.getString(SPKEY_TOKEN, null)
}
