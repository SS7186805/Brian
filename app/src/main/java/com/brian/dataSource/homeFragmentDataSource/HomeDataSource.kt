package com.brian.dataSource.homeFragmentDataSource

import com.brian.models.*

interface HomeDataSource {
    suspend fun getDefensive(): DefensiveResponse
    suspend fun submitAnswers(submitAnswerParams: SubmitAnswerParams): BaseResponse
    suspend fun getSelectDefensive(submitAnswerParams: SubmitAnswerParams): BaseResponse

    suspend fun questionResponse(id:Int) : QuestionResponse
    suspend fun getChallenges() : ResponseLeaderboard
    suspend fun getPlayers() : ResponseLeaderboard
    suspend fun getStats() : ResponseMyStats
    suspend fun getMyTeams( page:Int) : ResponseMyTeams
    suspend fun getData() : ResponseDataManagement
    suspend fun getGameSummary(id:Int) : ResponseGameSummary
    suspend fun createTeam(createTeam:CreateTeamParams) : ResponseCreateTeam

}