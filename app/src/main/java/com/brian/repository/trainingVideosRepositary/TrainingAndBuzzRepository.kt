package com.brian.repository.trainingVideosRepositary

import com.brian.models.QueryParams
import com.brian.models.ResponseBuzzFeed
import com.brian.models.ResponseDataManagement
import com.brian.models.ResponseTrainingVideosWithCategory

interface TrainingAndBuzzRepository {

    fun getTrainingVideos(
        queryParams: QueryParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseTrainingVideosWithCategory?
        ) -> Unit
    )

    fun getTrainingVideosDataManagement(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseDataManagement?
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