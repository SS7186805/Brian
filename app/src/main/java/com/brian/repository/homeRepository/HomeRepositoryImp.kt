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

    override fun getSelectDefensive(
        params: SubmitAnswerParams,
        onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = homeDataSource.getSelectDefensive(params)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }    }

    override fun submitAnswer(
        params: SubmitAnswerParams,
        onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = homeDataSource.submitAnswers(params)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }    }

    override fun questionResponse(id:Int,onResult: (isSuccess: Boolean, message: String, response: QuestionResponse?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = homeDataSource.questionResponse(id)
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
            val response = homeDataSource.createTeam(register)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getMyTeams( page:Int,onResult: (isSuccess: Boolean, message: String, response: ResponseMyTeams?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = homeDataSource.getMyTeams(page)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getData(onResult: (isSuccess: Boolean, message: String, response: ResponseDataManagement?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = homeDataSource.getData()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }    }
}
