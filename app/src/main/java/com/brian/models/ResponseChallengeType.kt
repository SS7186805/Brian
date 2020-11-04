package com.brian.models

import com.google.gson.annotations.SerializedName

data class ResponseChallengeType(

    @field:SerializedName("result")
    val result: String? = null,

    @field:SerializedName("data")
    val data: ChallengeTypeData? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    var error: String? = null
)

data class ChallengeTypeData(

    @field:SerializedName("first_page_url")
    val firstPageUrl: String? = null,

    @field:SerializedName("path")
    val path: String? = null,

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("data")
    val data: ArrayList<ChallengeTypeDataItem>? = null,

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

data class ChallengeTypeDataItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("challenge_name")
    val challengeName: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,



    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null
)