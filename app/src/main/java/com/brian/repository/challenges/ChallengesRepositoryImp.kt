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
        }    }


}