package com.brian.repository.challenges

import com.brian.dataSource.challenges.ChallengesDataSource
import com.brian.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChallengesRepositoryImp(private val challengesDataSource: ChallengesDataSource) :
    ChallengesRepository {


    override fun getChallenges(onResult: (isSuccess: Boolean, message: String, response: ResponseChallengeType?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = challengesDataSource.getChallenges()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getMyChallenges(onResult: (isSuccess: Boolean, message: String, response: ResponseMyChallenges?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = challengesDataSource.getMyChallenges()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getChallengesRequests(onResult: (isSuccess: Boolean, message: String, response: ResponseMyChallenges?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = challengesDataSource.getChallengesRequests()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun rejectChallengesRequests(
        queryParams: CreateChatRoomParams,
        onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = challengesDataSource.rejectChallengeRequest(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }    }

    override fun acceptChallengeRequests(
        queryParams: AcceptChallengeParams,
        onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = challengesDataSource.acceptChallengeRequest(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }    }

    override fun createChallenge(
        queryParams: CreateChallengeParams,
        onResult: (isSuccess: Boolean, message: String, response: ResponseCreateChallenge?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = challengesDataSource.createChallenge(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }


}