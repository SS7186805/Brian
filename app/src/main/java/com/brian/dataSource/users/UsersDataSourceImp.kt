package com.brian.dataSource.users

import com.brian.models.ResponseSearchUsers
import com.brian.models.ResponseSendRequest
import com.brian.models.SearchQuery
import com.brian.models.SendRequestParams
import com.brian.network.APIService

class UsersDataSourceImp(private val apiService: APIService) : UsersDataSource {


    override suspend fun getSearchUsers(queryParams: SearchQuery): ResponseSearchUsers {
        var response = ResponseSearchUsers()
        try {
            response = apiService.searchUsers(queryParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun sendRequest(queryParams: SendRequestParams): ResponseSendRequest {
        var response = ResponseSendRequest()
        try {
            response = apiService.sendFriendRequest(queryParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun cancelRequest(queryParams: SendRequestParams): ResponseSendRequest {
        var response = ResponseSendRequest()
        try {
            response = apiService.cancelFriendRequest(queryParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }
}