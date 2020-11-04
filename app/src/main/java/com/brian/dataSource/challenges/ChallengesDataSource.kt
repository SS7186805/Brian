package com.brian.dataSource.challenges

import com.brian.models.*

interface ChallengesDataSource {

    suspend fun getChallenges( id:Int): ResponseChallengeType
    suspend fun getMyChallenges(id:Int): ResponseMyChallenges
    suspend fun getChallengesRequests(id:Int): ResponseMyChallenges
    suspend fun createChallenge(queryParams: CreateChallengeParams): ResponseCreateChallenge
    suspend fun acceptChallengeRequest(queryParams: AcceptChallengeParams): BaseResponse
    suspend fun approveRejectMyChallenge(queryParams: AcceptChallengeParams): BaseResponse
    suspend fun cancelMyChallenge(queryParams: CreateChatRoomParams): BaseResponse
    suspend fun cancelRequest(queryParams: SendRequestParams): ResponseSendRequest
    suspend fun acceptRejectRequest(queryParams: SendRequestParams): ResponseSendRequest


}
