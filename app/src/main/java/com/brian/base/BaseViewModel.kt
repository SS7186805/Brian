package com.brian.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    var showMessage = MutableLiveData("")
    var showLoading = MutableLiveData(false)

}