package com.brian.network.dataSource.authDataSource

import com.brian.models.BaseResponse
import com.brian.models.DefensiveResponse
import com.brian.models.RegisterRequest
import com.brian.network.APIService
import com.brian.network.dataSource.authDataSource.AuthenticationDataSource
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthenticationDataSourceImp(private val apiService: APIService) : AuthenticationDataSource {
    override suspend fun SignUpResponse(registerRequest: RegisterRequest): BaseResponse {
        var response = BaseResponse()
        try {
            var params = HashMap<String, RequestBody>()
            params["name"] =  RequestBody.create("text/plain".toMediaTypeOrNull(), registerRequest.name!!)
            params["email"] = RequestBody.create("text/plain".toMediaTypeOrNull(), registerRequest.email!!)
            params["dob"] = RequestBody.create("text/plain".toMediaTypeOrNull(), registerRequest.dob!!)
            params["user_type"] = RequestBody.create("text/plain".toMediaTypeOrNull(), registerRequest.user_type!!)
            params["password"] = RequestBody.create("text/plain".toMediaTypeOrNull(), registerRequest.password!!)
            params["device_type"] = RequestBody.create("text/plain".toMediaTypeOrNull(), registerRequest.deviceType!!)
            params["device_token"] = RequestBody.create("text/plain".toMediaTypeOrNull(), registerRequest.deviceToken!!)

            response = apiService.signUp(params,registerRequest.profile_picture!!)
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
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }
}