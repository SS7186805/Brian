package com.brian.repository.usersRepositary

import com.brian.models.*

interface UsersRepository {

    fun searchUsers(
        queryParams: SearchQuery,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseSearchUsers?
        ) -> Unit
    )

    fun searchMyUsers(
        queryParams: SearchQuery,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseMyFriends?
        ) -> Unit
    )


    fun getMyFriends(
        page: Int,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseMyFriends?
        ) -> Unit
    )

    fun sendRequest(
        queryParams: SendRequestParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseSendRequest?
        ) -> Unit
    )

    fun cancelRequest(
        queryParams: SendRequestParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseSendRequest?
        ) -> Unit
    )

    fun acceptRejectRequest(
        queryParams: SendRequestParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseSendRequest?
        ) -> Unit
    )


    fun removeFriend(
        queryParams: SendRequestParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: BaseResponse?
        ) -> Unit
    )


}