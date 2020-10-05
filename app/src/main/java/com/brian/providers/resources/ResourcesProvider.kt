package com.brian.providers.resources

import android.graphics.Typeface

interface ResourcesProvider {
    fun getString(id: Int): String
    fun getStringArray(id: Int): Array<String>
    fun getDeviceId(): String
    fun getColor(id: Int): Int
    fun getFont(id: Int): Typeface
    fun getInt(id: Int): Int
}