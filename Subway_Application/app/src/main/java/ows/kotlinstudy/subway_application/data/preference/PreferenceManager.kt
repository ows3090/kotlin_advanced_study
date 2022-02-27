package ows.kotlinstudy.subway_application.data.preference

interface PreferenceManager {

    fun getLong(key: String): Long?

    fun putLong(key: String, value: Long)
}