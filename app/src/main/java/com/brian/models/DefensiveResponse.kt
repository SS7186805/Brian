package com.brian.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class DefensiveResponse(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("data")
	val data: DefensiveData? = null,

	@field:SerializedName("error")
	var error: String? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable


@Parcelize
data class DataItem(

	@field:SerializedName("situation_name")
	val situationName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: @RawValue Any? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: @RawValue Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: @RawValue Any? = null
) : Parcelable



@Parcelize
data class DefensiveData(

	@field:SerializedName("first_page_url")
	val firstPageUrl: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	@field:SerializedName("last_page")
	val lastPage: Int? = null,

	@field:SerializedName("last_page_url")
	val lastPageUrl: String? = null,

	@field:SerializedName("next_page_url")
	val nextPageUrl:@RawValue Any? = null,

	@field:SerializedName("from")
	val from: Int? = null,

	@field:SerializedName("to")
	val to: Int? = null,

	@field:SerializedName("prev_page_url")
	val prevPageUrl:@RawValue Any? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
) : Parcelable
