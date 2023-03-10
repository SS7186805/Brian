package com.brian.viewModels.myProfile

import android.text.TextUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.brian.base.BaseViewModel
import com.brian.base.Prefs
import com.brian.models.ChangePassword
import com.brian.models.LoginData
import com.brian.repository.myProfileRepository.MyProfileRepository

class MyProfileViewModel(private val myProfileRepository: MyProfileRepository) : BaseViewModel() {

    var change = ObservableField<ChangePassword>(ChangePassword())
    var loginData = MutableLiveData<LoginData>()


    fun changePassword() {
        if (validation()) {
            showLoading.postValue(true)
            myProfileRepository.changePassword(change.get()!!) { isSuccess, message, response ->
                if (isSuccess) {
                    showLoading.postValue(false)
                    //  showMessage.postValue(response?.message)
                    registerSuccess.postValue(true)
                } else {
                    showLoading.postValue(false)
                    registerSuccess.postValue(false)
                    showMessage.postValue(message)
                }
            }
        }
    }

    fun validation(): Boolean {
        if (TextUtils.isEmpty(change.get()!!.old_password)) {
            showMessage.postValue("Please enter old password.")
            return false
        } else if (TextUtils.isEmpty(change.get()!!.new_password)) {
            showMessage.postValue("Please enter new pasword.")
            return false
        } else if (change.get()!!.new_password!!.length < 6) {
            showMessage.postValue("Password must be at least 6 characters long.")
            return false
        } else if (TextUtils.isEmpty(change.get()!!.confirm_password)) {
            showMessage.postValue("Please enter confirm pasword.")
            return false
        } else if (change.get()!!.new_password != change.get()!!.confirm_password) {
            showMessage.postValue("New password and confirm password doesn't match.")
            return false
        }
        return true
    }

    fun viewProfile(id: String) {
        myProfileRepository.viewProfile(id) { isSuccess, message, response ->

            if (isSuccess) {

                if (response?.data is LoginData) {
                    val loginData: LoginData = response?.data

                    Log.e("UserId",Prefs.init().userInfo?.id.toString())
                    Log.e("OtherUser",response.data.id.toString())

                    if (Prefs.init().userInfo?.id.toString().equals(response.data.id.toString())) {
                        Log.e("OwnUser",response.data.id.toString())
                        Prefs.init().userInfo = loginData
                    }

                    this@MyProfileViewModel.loginData.postValue(loginData)
                }
            } else {
                showMessage.postValue(message)
            }

        }
    }
}