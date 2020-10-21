package com.brian.viewModels.myProfile

import android.text.TextUtils
import androidx.databinding.ObservableField
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.models.ChangePassword
import com.brian.models.QuestionData
import com.brian.repository.authRepository.myProfileRepository.MyProfileRepository

class MyProfileViewModel(private val myProfileRepository: MyProfileRepository) : BaseViewModel() {

    var change = ObservableField<ChangePassword>(ChangePassword())

//    init {
//        change.get()?.apply {
//            old_password = "123456"
//            new_password = "1234567"
//            confirm_password = "1234567"
//        }
//    }

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
        } else if (change.get()!!.new_password!!.length<6){
            showMessage.postValue("Password must be at least 6 characters long.")
            return false
        }else if (TextUtils.isEmpty(change.get()!!.confirm_password)) {
            showMessage.postValue("Please enter confirm pasword.")
            return false
        }else if (change.get()!!.new_password != change.get()!!.confirm_password) {
            showMessage.postValue("New password and confirm password doesn't match.")
            return false
        }
        return true
    }
}