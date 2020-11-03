package com.brian.models

import com.google.gson.annotations.SerializedName

data class ResponseTrainingVideosWithCategory(

    @field:SerializedName("result")
    val result: String? = null,

    @field:SerializedName("data")
    val data: TrainingData? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    var error: String? = null

)

data class TrainingData(

    @field:SerializedName("category_name")
    val categoryName: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: Any? = null,

    @field:SerializedName("training_videos")
    val trainingVideos: TrainingVideos? = null,

    @field:SerializedName("created_at")
    val createdAt: Any? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null
)

data class TrainingVideos(

    @field:SerializedName("first_page_url")
    val firstPageUrl: String? = null,

    @field:SerializedName("path")
    val path: String? = null,

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("data")
    val data: ArrayList<TrainingVideosCategoryDataItem>? = null,

    @field:SerializedName("last_page")
    val lastPage: Int? = null,

    @field:SerializedName("last_page_url")
    val lastPageUrl: String? = null,

    @field:SerializedName("next_page_url")
    val nextPageUrl: Any? = null,

    @field:SerializedName("from")
    val from: Int? = null,

    @field:SerializedName("to")
    val to: Int? = null,

    @field:SerializedName("prev_page_url")
    val prevPageUrl: Any? = null,

    @field:SerializedName("current_page")
    val currentPage: Int? = null
)

data class TrainingVideosCategoryDataItem(

    @field:SerializedName("video_url")
    val videoUrl: String? = null,

    @field:SerializedName("category_id")
    val categoryId: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("thumbnail_video")
    val thumbnailVideo: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null
)
