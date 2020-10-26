package com.brian.dataSource.trainingDataSource

import com.brian.models.QueryParams
import com.brian.models.ResponseBuzzFeed
import com.brian.models.ResponseTrainingVideos
import com.brian.network.APIService

class TrainingAndBuzzDataSourceImp(private val apiService: APIService) : TrainingAndBuzzDataSource {


    override suspend fun getTrainingVideos(queryParams: QueryParams): ResponseTrainingVideos {
        var response = ResponseTrainingVideos()
        try {
            response = apiService.getTrainingVideos(queryParams.page)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getBuzzFeed(queryParams: QueryParams): ResponseBuzzFeed {
        var response = ResponseBuzzFeed()
        try {
            response = apiService.getBuzzFeed(queryParams.page)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response    }
}