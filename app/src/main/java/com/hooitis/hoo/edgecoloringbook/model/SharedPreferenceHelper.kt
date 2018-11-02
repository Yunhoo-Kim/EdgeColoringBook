package com.hooitis.hoo.edgecoloringbook.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject
import javax.inject.Named

@Suppress("unused")
class SharedPreferenceHelper @Inject constructor(@Named("appContext")private val context: Context){
    enum class KEY(val key:String) {
        ACCESS_TOKEN(key = "ACCESS_TOKEN"),
        USER_ID(key = "USER_ID")
    }

    fun setString(key: KEY, value: String){
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        editor.putString(key.key, value)
        editor.apply()
    }

    fun setInt(key: KEY, value: Int){
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        editor.putInt(key.key, value)
        editor.apply()
    }

    fun setLong(key: KEY, value: Long){
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        editor.putLong(key.key, value)
        editor.apply()
    }

    fun setFloat(key: KEY, value: Float){
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        editor.putFloat(key.key, value)
        editor.apply()
    }

    fun setBoolean(key: KEY, value: Boolean){
        val settings = context.getSharedPreferences(PREF_FILE, 0)
        val editor = settings.edit()
        editor.putBoolean(key.key, value)
        editor.apply()
    }

    fun getString(key: KEY, default:String=""): String{
        val settings = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        return settings.getString(key.key, default)
    }

    fun getInt(key: KEY, default:Int=0): Int{
        val settings = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        return settings.getInt(key.key, default)
    }

    fun getFloat(key: KEY, default:Float=0.0f): Float{
        val settings = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        return settings.getFloat(key.key, default)
    }

    fun getBoolean(key: KEY, default:Boolean=false): Boolean{
        val settings = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        return settings.getBoolean(key.key, default)
    }

    fun exists(key: KEY) = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE).contains(key.key)

    fun clear() = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE).edit().clear().apply()


    companion object {
        const val PREF_FILE = "SHARED_INFO"
    }

}
