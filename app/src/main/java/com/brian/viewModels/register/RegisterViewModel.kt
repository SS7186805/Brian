package com.brian.viewModels.register


import android.text.TextUtils
import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.models.AuthRequest
import com.brian.models.RegisterRequest
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.authRepository.AuthenticationRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody


class RegisterViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {


    var registerSuccess = MutableLiveData<Boolean>()
    //var registerRequest = ObservableField<RegisterRequest>(RegisterRequest())
//    var authRequest = ObservableField<AuthRequest>(AuthRequest())
    var authRequest = ObservableField<RegisterRequest>(RegisterRequest())

    fun setValue(){
//        registerRequest.name = getText(authRequest.get()?.name!!)
//        registerRequest.email = getText(authRequest.get()?.email!!)
//        registerRequest.user_type = getText(authRequest.get()?.user_type!!)
//        registerRequest.dob = getText(authRequest.get()?.dob!!)
//        registerRequest.password = getText(authRequest.get()?.password!!)
//        registerRequest.cnf_password = getText(authRequest.get()?.cnf_password!!)
//        registerRequest.deviceType=getText("Android")
//        registerRequest.deviceToken = getText("Asdasda")
    }
    fun getText(text: String):MultipartBody.Part{
        val text = MultipartBody.Part.createFormData(
            "text",
            text,
            RequestBody.create("text/*".toMediaTypeOrNull(), text)
        )
        return text
    }

    init {
        authRequest.get()?.apply {
            name = "shivam"
            email = "uic.17bca14021@gmail.com"
            dob = "2020-10-01"
            user_type = "Players"
            password = "123456"
            cnf_password = "123456"
            deviceType = "Android"
            deviceToken ="Asdasda"
        }
    }

    fun onSignUpClick() {
        if (SignUpvalidate()) {
            setValue()
            showLoading.postValue(true)
            authenticationRepository.SignUpResponse(authRequest.get()!!)
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
        if (TextUtils.isEmpty(authRequest.get()!!.name)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_your_name))
            return false
        } else if (TextUtils.isEmpty(authRequest.get()!!.email)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_your_email))
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(authRequest.get()!!.email).matches()) {
            showMessage.postValue(resourcesProvider.getString(R.string.Valid_Email))
            return false
        } else if (TextUtils.isEmpty(authRequest.get()!!.dob)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_your_date_birth))
            return false
        } else if (TextUtils.isEmpty(authRequest.get()!!.user_type)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Select_User_type))
            return false
        } else if (TextUtils.isEmpty(authRequest.get()!!.password)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_passowrd))
            return false
        } else if (TextUtils.isEmpty(authRequest.get()!!.cnf_password)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_confirm_password))
            return false
        } else if (authRequest.get()!!.password != authRequest.get()!!.cnf_password) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_same_password))
            return false
        }
        return true
    }

    fun onLoginClick() {
        if (loginValidation()) {
            showLoading.postValue(true)
            authenticationRepository.LoginResponse(authRequest.get()!!)
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
        if (TextUtils.isEmpty(authRequest.get()!!.email)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_email_address))
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(authRequest.get()!!.email).matches()) {
            showMessage.postValue(resourcesProvider.getString(R.string.Valid_Email))
            return false
        } else if (TextUtils.isEmpty(authRequest.get()!!.password)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_password))
            return false
        }
        return true
    }

    fun onSendClick() {
        if (forgotValidation()) {
            showLoading.postValue(true)
            authenticationRepository.ForgotResponse(authRequest.get()!!)
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

    fun forgotValidation(): Boolean {

        if (TextUtils.isEmpty(authRequest.get()!!.email)) {
            showMessage.postValue(resourcesProvider.getString(R.string.Enter_email_address))
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(authRequest.get()!!.email).matches()) {
            showMessage.postValue(resourcesProvider.getString(R.string.Valid_Email))
            return false
        }
        return true
    }

}