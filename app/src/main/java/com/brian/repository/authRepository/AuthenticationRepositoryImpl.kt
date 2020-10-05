package com.brian.repository.authRepository

import com.brian.models.BaseResponse
import com.brian.models.RegisterRequest
import com.brian.network.dataSource.AuthenticationDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthenticationRepositoryImpl(private val authenticationDataSource: AuthenticationDataSource) :
    AuthenticationRepository {

    override fun SignUpResponse(registerRequest: RegisterRequest) : BaseResponse {
        var response = BaseResponse()
        GlobalScope.launch(Dispatchers.Main) {
             response = authenticationDataSource.SignUpResponse(registerRequest)
        }
        return response
    }
    /*GlobalScope.launch(Dispatchers.Main) {
        val response = flightsDataSource.getCompanies(queryParams)
        onResult(response.aerocrs!!.success, response)
    }
*/
    override fun LoginResponse(registerRequest: RegisterRequest):BaseResponse {
      var response = BaseResponse()
        GlobalScope.launch(Dispatchers.Main) {
             response = authenticationDataSource.LoginResponse(registerRequest)
        }
        return response
    }

    override fun ForgotResponse(registerRequest: RegisterRequest): BaseResponse {
        var response = BaseResponse()
        GlobalScope.launch (Dispatchers.Main){
            response = authenticationDataSource.ForgotResponse(registerRequest)
        }
        return response
    }
}