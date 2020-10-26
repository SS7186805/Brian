package com.brian.dataSource.homeFragmentDataSource

import com.brian.models.DefensiveResponse
import com.brian.models.QuestionResponse

interface HomeDataSource {
    suspend fun getDefensive(): DefensiveResponse

    suspend fun questionResponse() : QuestionResponse

}