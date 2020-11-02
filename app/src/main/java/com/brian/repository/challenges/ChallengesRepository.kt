package com.brian.repository.challenges

import com.brian.models.*

interface ChallengesRepository {

    fun getChallenges(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseChallengeType?
        ) -> Unit
    )


    fun getMyChallenges(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseMyChallenges?
        ) -> Unit
    )


    fun getChallengesRequests(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseMyChallenges?
        ) -> Unit
    )


    fun rejectChallengesRequests(
        queryParams: CreateChatRoomParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: BaseResponse?
        ) -> Unit
    )

    fun acceptChallengeRequests(
        queryParams: AcceptChallengeParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: BaseResponse?
        ) -> Unit
    )


    fun createChallenge(
        queryParams: CreateChallengeParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseCreateChallenge?
        ) -> Unit
    )

    /*
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
    )*/


}