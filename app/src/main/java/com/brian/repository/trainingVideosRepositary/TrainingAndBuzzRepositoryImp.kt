package com.brian.repository.trainingVideosRepositary

import com.brian.dataSource.trainingDataSource.TrainingAndBuzzDataSource
import com.brian.models.QueryParams
import com.brian.models.ResponseBuzzFeed
import com.brian.models.ResponseTrainingVideos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TrainingAndBuzzRepositoryImp(private val trainingDataSource: TrainingAndBuzzDataSource) :
    TrainingAndBuzzRepository {


    override fun getTrainingVideos(
        queryParams: QueryParams,
        onResult: (isSuccess: Boolean, message: String, response: ResponseTrainingVideos?) -> Unit
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