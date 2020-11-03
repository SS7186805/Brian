package com.brian.dataSource.trainingDataSource

import com.brian.models.*

interface TrainingAndBuzzDataSource {

    suspend fun getTrainingVideos(queryParams: QueryParams) : ResponseTrainingVideosWithCategory
    suspend fun getData() : ResponseDataManagement
    suspend fun getBuzzFeed(queryParams: QueryParams) : ResponseBuzzFeed




}
