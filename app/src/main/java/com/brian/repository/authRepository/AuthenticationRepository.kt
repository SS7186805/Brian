package com.brian.repository.authRepository

import androidx.databinding.ObservableField
import com.brian.models.BaseResponse
import com.brian.models.RegisterRequest

interface AuthenticationRepository{

    fun SignUpResponse(
        registerRequest: RegisterRequest,
                       onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit)

    fun LoginResponse(registerRequest: RegisterRequest) : BaseResponse

    fun ForgotResponse(registerRequest: RegisterRequest) : BaseResponse
}