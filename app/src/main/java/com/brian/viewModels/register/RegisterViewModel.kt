package com.brian.viewModels.register


import android.text.TextUtils
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
          val response = authenticationRepository.SignUpResponse(registerRequest.get()!!)
            println(response.result)
            if (response.result == resourcesProvider.getString(R.string.success))
                registerSuccess.postValue(true)

            showMessage.postValue(response.message)
        }
    }

    fun SignUpvalidate(): Boolean {
        if (TextUtils.isEmpty(registerRequest.get()!!.name)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_your_name))
            return false
        } else if (TextUtils.isEmpty(registerRequest.get()!!.email)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_your_email))
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
            val response = authenticationRepository.LoginResponse(registerRequest.get()!!)
        }
    }

    fun loginValidation(): Boolean {
        if (TextUtils.isEmpty(registerRequest.get()!!.email)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_email_address))
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
            var response = authenticationRepository.ForgotResponse(registerRequest.get()!!)
        }
    }

    fun forgotValidation(): Boolean {

        if (TextUtils.isEmpty(registerRequest.get()!!.email)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_email_address))
            return false
        }
        return true
    }

}