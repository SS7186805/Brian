package com.brian.base

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import com.brian.models.LoginData

import com.google.gson.Gson

class Prefs {

    private val PREF_NAME_GLOBAL = "GLOBAL"
    private val IS_LOG_IN = "IS_LOG_IN"
    private val ACCESS_TOKEN = "ACCESS_TOKEN"
    private val USER_INFO = "user_info"


    private var pref: SharedPreferences = MainApplication.get().applicationContext
        .getSharedPreferences(PREF_NAME_GLOBAL, MODE_PRIVATE)


    init {
        instance = this
    }

    val gson = Gson()

    companion object {
        private var instance: Prefs? = null
        fun init(): Prefs {
            if (instance == null) {
                instance = Prefs()
            }
            return instance!!
        }
    }

    fun clear() {
        pref.edit().clear().apply()
    }

    /*Authentication Token String for API Authentication*/
    /*var authenticationToken : String
    get() {return sharedPreferences.getString(PREF_AUTH_TOKEN,"") ?: ""}
    set(value) {sharedPreferences.edit().putString(PREF_AUTH_TOKEN,value).apply()}*/


    //Device Token, saved in separate SharedPreferences {PREF_NAME_GLOBAL} to persist data on user logout
    var isLogIn: String
        get() {
            return pref.getString(IS_LOG_IN, "") ?: ""
        }
        set(value) {
            pref.edit().putString(IS_LOG_IN, value).apply()
        }


    var accessToken: String
        get() {
            return pref.getString(ACCESS_TOKEN, "") ?: ""
        }
        set(value) {
            pref.edit().putString(ACCESS_TOKEN, value).apply()
        }

    var userInfo: LoginData?
        get() {
            var data: LoginData? = null
            val prefString = pref.getString(USER_INFO, "")
            if (!TextUtils.isEmpty(prefString)) {
                data = Gson().fromJson(prefString, LoginData::class.java)
            }
            return data
        }
        set(value) {
            var prefString = ""
            if (value != null) {
                prefString = Gson().toJson(value)
            }
            pref.edit().putString(USER_INFO, prefString).apply()
        }


}