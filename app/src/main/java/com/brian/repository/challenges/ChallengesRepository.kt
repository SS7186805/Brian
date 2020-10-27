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