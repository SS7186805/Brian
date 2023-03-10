package com.brian.repository.authRepository

import com.brian.models.BaseResponse
import com.brian.models.ContactUsParams
import com.brian.models.RegisterRequest

interface AuthenticationRepository{

    fun SignUpResponse(
        registerRequest: RegisterRequest,
                       onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit)

    fun  LoginResponse(registerRequest: RegisterRequest,
                      onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit)

    fun ForgotResponse(registerRequest: RegisterRequest,
                       onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit)

    fun logOutResponse(onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit)
    fun contactUs(contactUsParams: ContactUsParams,onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit)

    fun editProfile(register:RegisterRequest,onResult: (
        isSuccess: Boolean,
        message: String,
        response: BaseResponse?
    ) -> Unit)


}