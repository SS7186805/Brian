package com.brian.repository.homeRepository

import com.brian.models.*

interface HomeRepository {
    fun getDefensive(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: DefensiveResponse?
        ) -> Unit
    )

    fun questionResponse(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: QuestionResponse?
        ) -> Unit
    )

    fun getChallenges(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseLeaderboard?
        ) -> Unit
    )

    fun getPlayers(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseLeaderboard?
        ) -> Unit
    )

    fun getMyStats(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseMyStats?
        ) -> Unit
    )

    fun createTeam(
        register: CreateTeamParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseCreateTeam?
        ) -> Unit
    )

    fun getMyTeams(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseMyTeams?
        ) -> Unit
    )


}