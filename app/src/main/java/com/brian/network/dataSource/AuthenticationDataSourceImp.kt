package com.brian.network.dataSource

import com.brian.models.BaseResponse
import com.brian.models.RegisterRequest
import com.brian.network.APIService
import com.brian.network.BASE_URL

class AuthenticationDataSourceImp(private val apiService: APIService) : AuthenticationDataSource {
    override suspend fun SignUpResponse(registerRequest: RegisterRequest): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.signUp(registerRequest)
            println(response)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    override suspend fun LoginResponse(registerRequest: RegisterRequest): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.login(registerRequest)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return response
    }

    override suspend fun ForgotResponse(registerRequest: RegisterRequest): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.forgot(registerRequest)
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
        return response
    }
}