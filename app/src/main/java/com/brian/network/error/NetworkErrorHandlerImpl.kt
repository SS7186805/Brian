package com.ayuka.mvvmdemo.data.network.error

import com.brian.R
import com.brian.providers.resources.ResourcesProvider
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkErrorHandlerImpl(private val resources: ResourcesProvider) : NetworkErrorHandler {
    override fun getErrorMessage(e: Exception): String {
        var error = ""
        try {
            error = when (e) {
                is HttpException -> e.message()
                is ConnectException -> resources.getString(R.string.connectErr)
                is SocketTimeoutException -> resources.getString(R.string.timeoutErr)
                is UnknownHostException -> resources.getString(R.string.noInternetErr)
                else -> ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            return error
        }
    }
}