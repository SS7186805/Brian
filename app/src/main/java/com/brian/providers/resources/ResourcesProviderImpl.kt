package com.brian.providers.resources

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.provider.Settings
import androidx.core.content.res.ResourcesCompat
import com.brian.providers.resources.ResourcesProvider

class ResourcesProviderImpl(private val context: Context) : ResourcesProvider {
    override fun getString(id: Int): String {
        return context.getString(id)
    }
    override fun getContext(): Context {
        return context
    }


    override fun getStringArray(id: Int): Array<String> {
        return context.resources.getStringArray(id)
    }

    @SuppressLint("HardwareIds")
    override fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    override fun getColor(id: Int): Int {
        return context.getColor(id)
    }

    override fun getFont(id: Int): Typeface {
        return ResourcesCompat.getFont(context, id)!!
    }

    override fun getInt(id: Int): Int {
        return context.resources.getInteger(id)
    }
}