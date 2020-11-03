package com.brian.models

import com.google.gson.annotations.SerializedName

data class ResponseDataManagement(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("data")
	val data: ArrayList<DataManagementDataItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	var error: String? = null
)

data class DataManagementDataItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("heading")
	val heading: String? = null,

	@field:SerializedName("file_name")
	val fileName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("description_1")
	val description1: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("description_2")
	val description2: String? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null
)