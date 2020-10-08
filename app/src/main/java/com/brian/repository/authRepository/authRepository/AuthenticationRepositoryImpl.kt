package com.brian.repository.authRepository.authRepository

import android.util.Log
import com.brian.models.BaseResponse
import com.brian.models.DefensiveResponse
import com.brian.models.RegisterRequest
import com.brian.network.dataSource.authDataSource.AuthenticationDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthenticationRepositoryImpl(private val authenticationDataSource: AuthenticationDataSource) :
    AuthenticationRepository {

    override fun SignUpResponse(
        registerRequest: RegisterRequest,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: BaseResponse?
        ) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {

            val response = authenticationDataSource.SignUpResponse(registerRequest)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun LoginResponse(
        registerRequest: RegisterRequest,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: BaseResponse?
        ) -> Unit
    ) {

        GlobalScope.launch(Dispatchers.Main) {
            val response = authenticationDataSource.LoginResponse(registerRequest)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun ForgotResponse(
        registerRequest: RegisterRequest,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: BaseResponse?
        ) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = authenticationDataSource.ForgotResponse(registerRequest)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }

    }

    override fun logOutResponse(onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main){
            val response = authenticationDataSource.LogOutResponse()
            if(response.error != null){
                onResult(false,response.error!!,null)
            }else{
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun editProfile(
        register: RegisterRequest,
        onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main){
            val response = authenticationDataSource.editProfile(register)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }
}