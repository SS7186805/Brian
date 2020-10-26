package com.brian.dataSource.users

import com.brian.models.*

interface UsersDataSource {

    suspend fun getSearchUsers(queryParams: SearchQuery) : ResponseSearchUsers
    suspend fun sendRequest(queryParams: SendRequestParams) : ResponseSendRequest
    suspend fun cancelRequest(queryParams: SendRequestParams) : ResponseSendRequest
    suspend fun acceptSendRequest(queryParams: SendRequestParams) : ResponseSendRequest
    suspend fun myFriends() : ResponseSendRequest




}
