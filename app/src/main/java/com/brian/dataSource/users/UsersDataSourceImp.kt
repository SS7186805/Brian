package com.brian.dataSource.users

import com.brian.models.*
import com.brian.network.APIService

class UsersDataSourceImp(private val apiService: APIService) : UsersDataSource {


    override suspend fun getSearchUsers(queryParams: SearchQuery): ResponseSearchUsers {
        var response = ResponseSearchUsers()
        try {
            response = apiService.searchUsers(queryParams.page!!,queryParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getSearchMyUsers(queryParams: SearchQuery): ResponseMyFriends {
        var response = ResponseMyFriends()
        try {
            response = apiService.searchMyUsers(queryParams.page!!,queryParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getMyFriends(page:Int): ResponseMyFriends {
        var response = ResponseMyFriends()
        try {
            response = apiService.getMyFriends(page)
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

    override suspend fun acceptRejectRequest(queryParams: SendRequestParams): ResponseSendRequest {
        var response = ResponseSendRequest()
        try {
            response = apiService.acceptRejectRequest(queryParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun removeFriend(queryParams: SendRequestParams): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.removeFriend(queryParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response    }


}