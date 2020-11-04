package com.brian.dataSource.users

import com.brian.models.*

interface UsersDataSource {

    suspend fun getSearchUsers(queryParams: SearchQuery) : ResponseSearchUsers
    suspend fun getSearchMyUsers(queryParams: SearchQuery) : ResponseMyFriends
    suspend fun getMyFriends(page:Int) : ResponseMyFriends
    suspend fun sendRequest(queryParams: SendRequestParams) : ResponseSendRequest
    suspend fun cancelRequest(queryParams: SendRequestParams) : ResponseSendRequest
    suspend fun acceptRejectRequest(queryParams: SendRequestParams) : ResponseSendRequest
    suspend fun removeFriend(queryParams: SendRequestParams) : BaseResponse




}
