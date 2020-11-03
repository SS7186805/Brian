package com.brian.dataSource.homeFragmentDataSource

import com.brian.models.*

interface HomeDataSource {
    suspend fun getDefensive(): DefensiveResponse

    suspend fun questionResponse() : QuestionResponse
    suspend fun getChallenges() : ResponseLeaderboard
    suspend fun getPlayers() : ResponseLeaderboard
    suspend fun getStats() : ResponseMyStats
    suspend fun getMyTeams() : ResponseMyTeams
    suspend fun getData() : ResponseDataManagement
    suspend fun createTeam(createTeam:CreateTeamParams) : ResponseCreateTeam

}