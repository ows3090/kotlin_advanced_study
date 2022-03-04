package ows.kotlinstudy.subway_application.data.preference

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * station_data.csv 변경시간 저장하는 sharedPreferenceManager 클래스
 */
class SharedPreferenceManager(
    private val sharedPreferences: SharedPreferences
): PreferenceManager {

    override fun getLong(key: String): Long? {
        val value = sharedPreferences.getLong(key, INVALID_LONG_VALUE)

        return if(value == INVALID_LONG_VALUE) null
            else value
    }

    override fun putLong(key: String, value: Long) {
        sharedPreferences.edit { putLong(key, value) }
    }

    companion object{
        private const val INVALID_LONG_VALUE = Long.MIN_VALUE
    }
}