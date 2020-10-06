package com.brian.network.dataSource

import androidx.databinding.ObservableField
import com.brian.models.AuthRequest
import com.brian.models.BaseResponse
import com.brian.models.RegisterRequest

interface AuthenticationDataSource  {

    suspend fun SignUpResponse(registerRequest: RegisterRequest):BaseResponse

    suspend fun LoginResponse(registerRequest: RegisterRequest):BaseResponse

    suspend fun ForgotResponse(registerRequest: RegisterRequest):BaseResponse

}