package com.brian.dataSource.homeFragmentDataSource

import com.brian.models.DefensiveResponse
import com.brian.models.QuestionResponse
import com.brian.network.APIService

class HomeDataSourceImp(private val apiService: APIService):HomeDataSource {

    override suspend fun getDefensive(): DefensiveResponse {
        var response = DefensiveResponse()
        try {
            response = apiService.getDefensiveSituation()
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun questionResponse(): QuestionResponse {
        var response  = QuestionResponse()
        try {
            response = apiService.questionResponse()
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }
}