package com.brian.dataSource.homeFragmentDataSource

import com.brian.models.*
import com.brian.network.APIService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody


class HomeDataSourceImp(private val apiService: APIService) : HomeDataSource {

    override suspend fun getDefensive(): DefensiveResponse {
        var response = DefensiveResponse()
        try {
            response = apiService.getDefensiveSituation()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun submitAnswers(submitAnswerParams: SubmitAnswerParams): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.submitAnswers(submitAnswerParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getSelectDefensive(submitAnswerParams: SubmitAnswerParams): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.getSelectDefensiveSituation(submitAnswerParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun questionResponse(id: Int): QuestionResponse {
        var response = QuestionResponse()
        try {
            response = apiService.questionResponse(id)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getChallenges(): ResponseLeaderboard {
        var response = ResponseLeaderboard()
        try {
            response = apiService.getChallengesLeaderBoard()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getPlayers(): ResponseLeaderboard {
        var response = ResponseLeaderboard()
        try {
            response = apiService.getPlayers()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getStats(): ResponseMyStats {
        var response = ResponseMyStats()
        try {
            response = apiService.getStats()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getMyTeams(page: Int): ResponseMyTeams {
        var response = ResponseMyTeams()
        try {
            response = apiService.getMyTeams(page)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getData(): ResponseDataManagement {
        var response = ResponseDataManagement()
        try {
            response = apiService.getDataHome()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun createTeam(createTeam: CreateTeamParams): ResponseCreateTeam {
        var response = ResponseCreateTeam()
        try {
            var params = HashMap<String, RequestBody>()
            params["team_name"] =
                RequestBody.create("text/plain".toMediaTypeOrNull(), createTeam.team_name!!)
            params["users"] =
                RequestBody.create("text/plain".toMediaTypeOrNull(), createTeam.users!!)


            response = apiService.createTeam(params, createTeam.image)


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }


}