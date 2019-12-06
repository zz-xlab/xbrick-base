package cn.zzstc.lzm.common.data

import android.content.Context
import cn.zzstc.lzm.common.BaseApp
import cn.zzstc.lzm.common.util.gson
import kotlin.properties.Delegates

object SpAccessor {
    const val SERVER_ADDRESS_INITED="server_address_inited"
    const val USER_INFO = "user_info"
    const val TOKEN = "token"
    private const val FILE_NAME = "lzm_sp"

    fun <T> put(key: String, value: T) {
        val sp = BaseApp.instance().getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sp.edit()
        when (value) {
            is String -> {
                editor.putString(key, value)
            }
            is Int -> {
                editor.putInt(key, value)
            }
            is Boolean -> {
                editor.putBoolean(key, value)
            }
            is Float -> {
                editor.putFloat(key, value)
            }
            is Long -> {
                editor.putLong(key, value)
            }
            else -> {
                editor.putString(key, gson.toJson(value))
            }
        }
        editor.apply()
    }

    fun getInt(key: String, defVal: Int): Int {
        val sp = BaseApp.instance().getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.getInt(key, defVal)
    }

    fun getString(key: String, defVal: String): String {
        val sp = BaseApp.instance().getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.getString(key, defVal) ?: defVal
    }

    fun getBool(key: String, defVal: Boolean): Boolean {
        val sp = BaseApp.instance().getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.getBoolean(key, defVal)
    }

    fun getFloat(key: String, defVal: Float): Float {
        val sp = BaseApp.instance().getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.getFloat(key, defVal)
    }

    fun getFloat(key: String, defVal: Long): Long {
        val sp = BaseApp.instance().getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.getLong(key, defVal)
    }

    fun <T> getObject(key: String, clazz: Class<T>): T {
        val str = getString(key, "{}")
        return gson.fromJson(str, clazz)
    }

}