package com.brian.network.dataSource.authDataSource

import com.brian.models.BaseResponse
import com.brian.models.DefensiveResponse
import com.brian.models.RegisterRequest
import com.brian.network.APIService
import com.brian.network.dataSource.authDataSource.AuthenticationDataSource

class AuthenticationDataSourceImp(private val apiService: APIService) : AuthenticationDataSource {
    override suspend fun SignUpResponse(registerRequest: RegisterRequest): BaseResponse {
        var response = BaseResponse()
        println(response)
        try {
            response = apiService.signUp(registerRequest)
            println(response)
        } catch (e: Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun LoginResponse(registerRequest: RegisterRequest): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.login(registerRequest)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun ForgotResponse(registerRequest: RegisterRequest): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.forgot(registerRequest)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun LogOutResponse(): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.logOut()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun editProfile(register: RegisterRequest): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.editProfile(register)
        }
        catch (e:java.lang.Exception){
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }
}