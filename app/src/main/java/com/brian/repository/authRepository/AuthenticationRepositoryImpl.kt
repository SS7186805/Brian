package com.brian.repository.authRepository

import android.util.Log
import com.brian.models.AuthRequest
import com.brian.models.BaseResponse
import com.brian.models.RegisterRequest
import com.brian.network.dataSource.AuthenticationDataSource
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

            Log.e("Response","${response.toString()}sds")
            Log.e("ResponseERRor","${response.error}sds")
            if (response.error!=null) {
                onResult(false, response.error!!.message!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun LoginResponse(registerRequest: RegisterRequest,
                               onResult: (
                                   isSuccess: Boolean,
                                   message: String,
                                   response: BaseResponse?
                               ) -> Unit) {

        GlobalScope.launch(Dispatchers.Main) {
          val  response = authenticationDataSource.LoginResponse(registerRequest)
            if (response.error!=null) {
                onResult(false, response.error!!.message!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun ForgotResponse(registerRequest: RegisterRequest,
                                onResult: (
                                    isSuccess: Boolean,
                                    message: String,
                                    response: BaseResponse?
                                ) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
          val  response = authenticationDataSource.ForgotResponse(registerRequest)
            if (response.error!=null) {
                onResult(false, response.error!!.message!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }

    }
}