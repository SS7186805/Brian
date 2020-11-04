package com.brian.models

import com.google.gson.annotations.SerializedName

data class ResponseMyStats(

    @field:SerializedName("result")
    val result: String? = null,

    @field:SerializedName("data")
    val data: DataMyStats? = null,

    @field:SerializedName("message")
    val message: String? = null,

	@field:SerializedName("error")
	var error: String? = null


)

data class DataMyStats(

    @field:SerializedName("defensive_avg")
    val defensiveAvg: Int? = null,

    @field:SerializedName("badges_earneda")
    val badgesEarneda: BadgesEarneday? = null,

    @field:SerializedName("challenges_completed")
    val challengesCompleted: Int? = null
)

data class BadgesEarneday(

    @field:SerializedName("first_page_url")
    val firstPageUrl: String? = null,

    @field:SerializedName("path")
    val path: String? = null,

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("data")
    val data: ArrayList<DataBadges>? = null,

    @field:SerializedName("last_page")
    val lastPage: Int? = null,

    @field:SerializedName("last_page_url")
    val lastPageUrl: String? = null,

    @field:SerializedName("next_page_url")
    val nextPageUrl: Any? = null,

    @field:SerializedName("from")
    val from: Any? = null,

    @field:SerializedName("to")
    val to: Any? = null,

    @field:SerializedName("prev_page_url")
    val prevPageUrl: Any? = null,

    @field:SerializedName("current_page")
    val currentPage: Int? = null
)