package com.brian.repository.trainingVideosRepositary

import com.brian.models.QueryParams
import com.brian.models.ResponseBuzzFeed
import com.brian.models.ResponseTrainingVideos

interface TrainingAndBuzzRepository {

    fun getTrainingVideos(
        queryParams: QueryParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseTrainingVideos?
        ) -> Unit
    )


    fun getBuzzFeed(
        queryParams: QueryParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseBuzzFeed?
        ) -> Unit
    )


}