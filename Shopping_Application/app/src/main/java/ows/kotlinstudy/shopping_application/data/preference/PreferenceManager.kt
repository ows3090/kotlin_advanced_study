package ows.kotlinstudy.shopping_application.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceManager(
    private val context: Context
) {

    companion object{
        const val PREFERECE_NAME = "shopping_application"
        private const val DEFAULT_VALUE_STRING = ""
        private const val DEFAULT_VALUE_BOOLEAN = false
        private const val DEFAULT_VALUE_INT = -1
        private const val DEFAULT_VALUE_LONG = -1L
        private const val DEFAULT_VALUE_FLOAT = -1f

        const val KEY_ID_TOKEN = "ID_TOKEN"
    }

    private fun getPreference(context: Context): SharedPreferences{
        return context.getSharedPreferences(PREFERECE_NAME, Context.MODE_PRIVATE)
    }

    private val prefs by lazy { getPreference(context) }

    private val editor by lazy { prefs.edit() }

    fun setString(key: String?, value: String?){
        editor.putString(key, value)
        editor.apply()
    }

    fun setBoolean(key: String?, value: Boolean){
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun setInt(key: String?, value: Int){
        editor.putInt(key, value)
        editor.apply()
    }

    fun setLong(key: String?, value: Long){
        editor.putLong(key, value)
        editor.apply()
    }

    fun setFloat(key: String?, value: Float){
        editor.putFloat(key, value)
        editor.apply()
    }

    fun getString(key: String?): String? {
        return prefs.getString(key, DEFAULT_VALUE_STRING)
    }

    fun getBoolean(key: String?): Boolean{
        return prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN)
    }

    fun getInt(key: String?): Int{
        return prefs.getInt(key, DEFAULT_VALUE_INT)
    }

    fun getLong(key: String?): Long{
        return prefs.getLong(key, DEFAULT_VALUE_LONG)
    }

    fun getFloat(key: String?): Float{
        return prefs.getFloat(key, DEFAULT_VALUE_FLOAT)
    }

    fun removeKey(key: String?){
        prefs.edit {
        }
        editor.remove(key)
        editor.apply()
    }

    fun clear(){
        editor.clear()
        editor.apply()
    }

    fun putIdToken(idToken: String){
        editor.putString(KEY_ID_TOKEN, idToken)
        editor.apply()
    }

    fun getIdToken(): String?{
        return prefs.getString(KEY_ID_TOKEN, null)
    }

    fun removeToken(){
        editor.putString(KEY_ID_TOKEN, null)
        editor.apply()
    }

}