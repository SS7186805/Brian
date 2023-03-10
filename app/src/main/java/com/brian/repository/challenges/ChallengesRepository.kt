package com.brian.repository.challenges

import com.brian.models.*

interface ChallengesRepository {

    fun getChallenges(
        id:Int,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseChallengeType?
        ) -> Unit
    )


    fun getMyChallenges(
        id:Int,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseMyChallenges?
        ) -> Unit
    )


    fun getChallengesRequests(
        id:Int,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseMyChallenges?
        ) -> Unit
    )


    fun cancelMyChallengesRequests(
        queryParams: CreateChatRoomParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: BaseResponse?
        ) -> Unit
    )

    fun acceptRejectChallengeRequests(
        queryParams: AcceptChallengeParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: BaseResponse?
        ) -> Unit
    )

    fun approveRejectMyChallenge(
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


}