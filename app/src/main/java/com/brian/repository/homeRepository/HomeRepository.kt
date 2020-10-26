package com.brian.repository.homeRepository

import com.brian.models.DefensiveResponse
import com.brian.models.QuestionResponse

interface HomeRepository {
    fun getDefensive(onResult: (
        isSuccess: Boolean,
        message: String,
        response: DefensiveResponse?
    ) -> Unit)

    fun questionResponse(onResult: (
        isSuccess: Boolean,
        message: String,
        response: QuestionResponse?
    ) -> Unit)

}