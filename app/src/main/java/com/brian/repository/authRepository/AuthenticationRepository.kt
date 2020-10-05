package com.brian.repository.authRepository

import androidx.databinding.ObservableField
import com.brian.models.BaseResponse
import com.brian.models.RegisterRequest

interface AuthenticationRepository{

    fun SignUpResponse(registerRequest: RegisterRequest) : BaseResponse
/*

    fun getCompanies(
        queryParams: FlightsApiAuth,
        onResult: (isSuccess: Boolean, baseResponse: FlightsCompanies) -> Unit
    )

*/

    fun LoginResponse(registerRequest: RegisterRequest) : BaseResponse

    fun ForgotResponse(registerRequest: RegisterRequest) : BaseResponse
}