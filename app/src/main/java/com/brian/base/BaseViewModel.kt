package com.brian.base

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brian.models.RegisterRequest

open class BaseViewModel : ViewModel(){

    var showMessage = MutableLiveData("")
    var showLoading = MutableLiveData(false)

}