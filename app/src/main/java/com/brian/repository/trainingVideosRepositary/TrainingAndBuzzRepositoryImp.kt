package com.brian.repository.trainingVideosRepositary

import com.brian.dataSource.trainingDataSource.TrainingAndBuzzDataSource
import com.brian.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TrainingAndBuzzRepositoryImp(private val trainingDataSource: TrainingAndBuzzDataSource) :
    TrainingAndBuzzRepository {


    override fun getTrainingVideos(
        queryParams: QueryParams,
        onResult: (isSuccess: Boolean, message: String, response: ResponseTrainingVideosWithCategory?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = trainingDataSource.getTrainingVideos(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getTrainingVideosDataManagement(onResult: (isSuccess: Boolean, message: String, response: ResponseDataManagement?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = trainingDataSource.getData()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getBuzzFeed(
        queryParams: QueryParams,
        onResult: (isSuccess: Boolean, message: String, response: ResponseBuzzFeed?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = trainingDataSource.getBuzzFeed(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }
}