package com.brian.repository.challenges

import com.brian.dataSource.challenges.ChallengesDataSource
import com.brian.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChallengesRepositoryImp(private val challengesDataSource: ChallengesDataSource) :
    ChallengesRepository {


    override fun getChallenges( id:Int,onResult: (isSuccess: Boolean, message: String, response: ResponseChallengeType?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = challengesDataSource.getChallenges(id)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getMyChallenges(id:Int,onResult: (isSuccess: Boolean, message: String, response: ResponseMyChallenges?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = challengesDataSource.getMyChallenges(id)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getChallengesRequests(id:Int,onResult: (isSuccess: Boolean, message: String, response: ResponseMyChallenges?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = challengesDataSource.getChallengesRequests(id)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun cancelMyChallengesRequests(
        queryParams: CreateChatRoomParams,
        onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = challengesDataSource.cancelMyChallenge(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun acceptRejectChallengeRequests(
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
        }
    }

    override fun approveRejectMyChallenge(
        queryParams: AcceptChallengeParams,
        onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = challengesDataSource.approveRejectMyChallenge(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }


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