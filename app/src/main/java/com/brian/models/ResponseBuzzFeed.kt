package com.brian.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponseBuzzFeed(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("data")
	val data: BuzzFeedData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	var error: String? = null
)

data class BuzzFeedData(

	@field:SerializedName("first_page_url")
	val firstPageUrl: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: ArrayList<BuzzFeedDataItem>? = null,

	@field:SerializedName("last_page")
	val lastPage: Int? = null,

	@field:SerializedName("last_page_url")
	val lastPageUrl: String? = null,

	@field:SerializedName("next_page_url")
	val nextPageUrl: String? = null,

	@field:SerializedName("from")
	val from: Int? = null,

	@field:SerializedName("to")
	val to: Int? = null,

	@field:SerializedName("prev_page_url")
	val prevPageUrl: Any? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)



@Parcelize
data class BuzzFeedDataItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("buzz_feed_files")
	val buzzFeedFiles: ArrayList<BuzzFeedFilesItem>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: String? = null
):Parcelable


@Parcelize
data class BuzzFeedFilesItem(

	@field:SerializedName("file_url")
	val fileUrl: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("buzz_feed_id")
	val buzzFeedId: Int? = null,

	@field:SerializedName("file_type")
	val fileType: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("thumbnail_url")
	val thumbnailUrl: String? = null
):Parcelable