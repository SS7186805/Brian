package com.brian.dataSource.challenges

import com.brian.models.*
import com.brian.network.APIService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class ChallenegesDataSourceImp(private val apiService: APIService) : ChallengesDataSource {


    override suspend fun getChallenges(): ResponseChallengeType {
        var response = ResponseChallengeType()
        try {
            response = apiService.getChallenges()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getMyChallenges(): ResponseMyChallenges {
        var response = ResponseMyChallenges()
        try {
            response = apiService.getMyChallenges()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getChallengesRequests(): ResponseMyChallenges {
        var response = ResponseMyChallenges()
        try {
            response = apiService.getChallengesRequests()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun createChallenge(queryParams: CreateChallengeParams): ResponseCreateChallenge {
        var response = ResponseCreateChallenge()
        try {
            response = apiService.createChallenge(queryParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun acceptChallengeRequest(queryParams: AcceptChallengeParams): BaseResponse {
        var response = BaseResponse()
        try {
            var params = HashMap<String, RequestBody>()
            params["user_challenge_id"] = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                queryParams.user_challenge_id.toString()!!
            )
            params["status"] = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                queryParams.status.toString()!!
            )
            response = apiService.acceptChallengeRequest(params, queryParams.file_name)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun approveRejectMyChallenge(queryParams: AcceptChallengeParams): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.approveRejectMyChallenge(queryParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun cancelMyChallenge(queryParams: CreateChatRoomParams): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.cancelMyChallenge(queryParams)
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


}