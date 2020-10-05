package com.brian.base

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils

import com.google.gson.Gson

class Prefs {

    private val PREF_NAME_GLOBAL = "GLOBAL"
    private val IS_LOG_IN = "IS_LOG_IN"
    private val ACCESS_TOKEN = "ACCESS_TOKEN"


    private var sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(MainApplication.get().applicationContext)

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
        sharedPreferences.edit().clear().apply()
    }

    /*Authentication Token String for API Authentication*/
    /*var authenticationToken : String
    get() {return sharedPreferences.getString(PREF_AUTH_TOKEN,"") ?: ""}
    set(value) {sharedPreferences.edit().putString(PREF_AUTH_TOKEN,value).apply()}*/


    //Device Token, saved in separate SharedPreferences {PREF_NAME_GLOBAL} to persist data on user logout
    var isLogIn: String
        get() {
            val sF = MainApplication.get().applicationContext
                .getSharedPreferences(PREF_NAME_GLOBAL, MODE_PRIVATE)
            return sF.getString(IS_LOG_IN, "") ?: ""
        }
        set(value) {
            val sF = MainApplication.get().applicationContext
                .getSharedPreferences(PREF_NAME_GLOBAL, MODE_PRIVATE)
            sF.edit().putString(IS_LOG_IN, value).apply()
        }


    var accessToken: String
        get() {
            val sF = MainApplication.get().applicationContext
                .getSharedPreferences(PREF_NAME_GLOBAL, MODE_PRIVATE)
            return sF.getString(ACCESS_TOKEN, "") ?: ""
        }
        set(value) {
            val sF = MainApplication.get().applicationContext
                .getSharedPreferences(PREF_NAME_GLOBAL, MODE_PRIVATE)
            sF.edit().putString(ACCESS_TOKEN, value).apply()
        }


}