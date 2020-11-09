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

    fun getSelectDefensive(
        params: SubmitAnswerParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: BaseResponse?
        ) -> Unit
    )

    fun submitAnswer(
        params: SubmitAnswerParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: BaseResponse?
        ) -> Unit
    )


    fun questionResponse(
        id: Int,
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
        page:Int,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseMyTeams?
        ) -> Unit
    )

    fun getData(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseDataManagement?
        ) -> Unit
    )

    fun getGameSummary(
        id: Int,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseGameSummary?
        ) -> Unit
    )


}