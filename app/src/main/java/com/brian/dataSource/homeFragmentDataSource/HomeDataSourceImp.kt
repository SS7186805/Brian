package com.brian.dataSource.homeFragmentDataSource

import com.brian.models.*
import com.brian.network.APIService

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

    override suspend fun questionResponse(): QuestionResponse {
        var response = QuestionResponse()
        try {
            response = apiService.questionResponse()
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

    override suspend fun createTeam(createTeam: CreateTeamParams): ResponseCreateTeam {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*  override suspend fun createTeam(createTeam: CreateTeamParams): ResponseCreateTeam {
          var response = ResponseMyStats()
          try {
              response = apiService.getStats()
          } catch (e: java.lang.Exception) {
              e.printStackTrace()
              response.error = APIService.getErrorMessageFromGenericResponse(e)
          }
          return response    }*/
}