package com.brian.dataSource.myProfileDataSource

import com.brian.models.*
import com.brian.network.APIService

class MyProfileDataSourceImp(private val apiService: APIService):MyProfileDataSource {
    override suspend fun changePassword(change:ChangePassword): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.changePassword(change)
        }
        catch (e:java.lang.Exception){
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun viewProfile(id:String): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.viewProfile(id)
        }
        catch (e:java.lang.Exception){
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }
}