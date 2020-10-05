package com.ayuka.mvvmdemo.data.network.error

interface NetworkErrorHandler {
    fun getErrorMessage(e: Exception): String
}