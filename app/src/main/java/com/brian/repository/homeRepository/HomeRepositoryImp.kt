package com.brian.repository.homeRepository

import com.brian.dataSource.homeFragmentDataSource.HomeDataSource
import com.brian.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeRepositoryImp(private val homeDataSource: HomeDataSource) : HomeRepository {

    override fun getDefensive(onResult: (isSuccess: Boolean, message: String, response: DefensiveResponse?) -> Unit) {

        GlobalScope.launch(Dispatchers.Main) {
            val response = homeDataSource.getDefensive()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }

    }

    override fun questionResponse(onResult: (isSuccess: Boolean, message: String, response: QuestionResponse?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = homeDataSource.questionResponse()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getChallenges(onResult: (isSuccess: Boolean, message: String, response: ResponseLeaderboard?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = homeDataSource.getChallenges()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getPlayers(onResult: (isSuccess: Boolean, message: String, response: ResponseLeaderboard?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = homeDataSource.getPlayers()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getMyStats(onResult: (isSuccess: Boolean, message: String, response: ResponseMyStats?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = homeDataSource.getStats()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun createTeam(
        register: CreateTeamParams,
        onResult: (isSuccess: Boolean, message: String, response: ResponseCreateTeam?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = homeDataSource.getStats()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
//                onResult(true, response.message!!, response)
            }
        }
    }
}
