package com.brian.dataSource.trainingDataSource

import com.brian.models.*
import com.brian.network.APIService

class TrainingAndBuzzDataSourceImp(private val apiService: APIService) : TrainingAndBuzzDataSource {


    override suspend fun getTrainingVideos(queryParams: QueryParams): ResponseTrainingVideosWithCategory {
        var response = ResponseTrainingVideosWithCategory()
        try {
            response = apiService.getTrainingVideosWithCategory(queryParams.page!!,queryParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getData(): ResponseDataManagement {
        var response = ResponseDataManagement()
        try {
            response = apiService.getDataTraining()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response    }

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