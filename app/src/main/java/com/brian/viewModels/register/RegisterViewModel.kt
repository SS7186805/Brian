package com.brian.viewModels.register


import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.models.BaseResponse
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.authRepository.AuthenticationRepository

class RegisterViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {


    var registerSuccess = MutableLiveData<Boolean>()

    init {
        registerRequest.get()?.apply {
            name = "shivam"
            email = "uic.17bca14021@gmail.com"
            dob = "2020-10-01"
            user_type = "Players"
            password = "123456"
            cnf_password = "123456"
            deviceType = "Android"
            deviceToken = "Asdasda"
        }
    }

    fun onSignUpClick() {
        if (SignUpvalidate()) {
            showLoading.postValue(true)
            authenticationRepository.SignUpResponse(registerRequest.get()!!)
            { isSuccess, message, response ->
                if (isSuccess) {
                    showLoading.postValue(false)
                    registerSuccess.postValue(true)
                    showMessage.postValue(response?.message)

                } else {
                    showLoading.postValue(false)
                    showMessage.postValue(message)
                }
            }
        }
    }

    fun SignUpvalidate(): Boolean {
        if (TextUtils.isEmpty(registerRequest.get()!!.name)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_your_name))
            return false
        } else if (TextUtils.isEmpty(registerRequest.get()!!.email)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_your_email))
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(registerRequest.get()!!.email).matches()) {
            showMessage.postValue(resourcesProvider.getString(R.string.Valid_Email))
            return false
        } else if (TextUtils.isEmpty(registerRequest.get()!!.dob)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_your_date_birth))
            return false
        } else if (TextUtils.isEmpty(registerRequest.get()!!.user_type)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Select_User_type))
            return false
        } else if (TextUtils.isEmpty(registerRequest.get()!!.password)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_passowrd))
            return false
        } else if (TextUtils.isEmpty(registerRequest.get()!!.cnf_password)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_confirm_password))
            return false
        } else if (registerRequest.get()!!.password != registerRequest.get()!!.cnf_password) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_same_password))
            return false
        }
        return true
    }

    fun onLoginClick() {
        if (loginValidation()) {
            showLoading.postValue(true)
            authenticationRepository.LoginResponse(registerRequest.get()!!)
            { isSuccess, message, response ->
                if (isSuccess) {
                    showLoading.postValue(false)
                    showMessage.postValue(response?.message)
                } else {
                    showLoading.postValue(false)
                    showMessage.postValue(message)
                }
            }
        }
    }

    fun loginValidation(): Boolean {
        if (TextUtils.isEmpty(registerRequest.get()!!.email)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_email_address))
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(registerRequest.get()!!.email).matches()) {
            showMessage.postValue(resourcesProvider.getString(R.string.Valid_Email))
            return false
        } else if (TextUtils.isEmpty(registerRequest.get()!!.password)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_password))
            return false
        }
        return true
    }

    fun onSendClick() {

        if (forgotValidation()) {
            showLoading.postValue(true)
            authenticationRepository.ForgotResponse(registerRequest.get()!!)
            {isSuccess, message, response ->
                if (isSuccess) {
                    showLoading.postValue(false)
                    showMessage.postValue(response?.message)

                } else {
                    showLoading.postValue(false)
                    showMessage.postValue(message)
                }
            }
        }
    }

    fun forgotValidation(): Boolean {

        if (TextUtils.isEmpty(registerRequest.get()!!.email)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_email_address))
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(registerRequest.get()!!.email).matches()) {
            showMessage.postValue(resourcesProvider.getString(R.string.Valid_Email))
            return false
        }
        return true
    }

}