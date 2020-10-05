package com.brian.viewModels.login

import android.text.TextUtils
import com.brian.base.BaseViewModel
import com.brian.repository.authRepository.AuthenticationRepository

class LoginViewModel( private val authenticationRepository: AuthenticationRepository) : BaseViewModel(){

//    fun onLoginClick() {
//
//        if (loginValidation()) {
//            println("registerRequest.get() = ${registerRequest.get()}")
//
//            val response = authenticationRepository.LoginResponse(registerRequest.get()!!)
//
//            println(response)
//
//        }
//    }
//    fun loginValidation(): Boolean {
//
//        if (TextUtils.isEmpty(registerRequest.get()!!.email)) {
//            RegisterToast.postValue("Enter Email Address")
//            return false
//        } else if (TextUtils.isEmpty(registerRequest.get()!!.password)) {
//            RegisterToast.postValue("Enter password")
//            return false
//        }
//        return true
//    }

}