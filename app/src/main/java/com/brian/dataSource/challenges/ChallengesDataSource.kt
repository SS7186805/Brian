package com.brian.dataSource.challenges

import com.brian.models.*

interface ChallengesDataSource {

    suspend fun getChallenges(): ResponseChallengeType
    suspend fun getMyChallenges(): ResponseMyChallenges
    suspend fun getChallengesRequests(): ResponseMyChallenges
    suspend fun createChallenge(queryParams: CreateChallengeParams): ResponseCreateChallenge
    suspend fun acceptChallengeRequest(queryParams: AcceptChallengeParams): BaseResponse
    suspend fun rejectChallengeRequest(queryParams: CreateChatRoomParams): BaseResponse
    suspend fun cancelRequest(queryParams: SendRequestParams): ResponseSendRequest
    suspend fun acceptRejectRequest(queryParams: SendRequestParams): ResponseSendRequest


}
