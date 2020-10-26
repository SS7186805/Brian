package com.brian.dataSource.trainingDataSource

import com.brian.models.*

interface TrainingAndBuzzDataSource {

    suspend fun getTrainingVideos(queryParams: QueryParams) : ResponseTrainingVideos
    suspend fun getBuzzFeed(queryParams: QueryParams) : ResponseBuzzFeed




}
