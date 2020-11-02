package com.brian.models

import com.google.gson.annotations.SerializedName

data class ResponseLeaderboard(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("data")
	val data: ArrayList<LeaderboardDataItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,


	@field:SerializedName("error")
	var error: String? = null

)
data class LeaderboardDataItem(

	@field:SerializedName("is_block")
	val isBlock: Int? = null,

	@field:SerializedName("is_verify")
	val isVerify: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("device_type")
	val deviceType: Any? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("weekly_situation")
	val weeklySituation: Double? = null,

	@field:SerializedName("weekly_complete_challenge")
	val weeklyCompleteChallenge: Int? = 0,

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("device_token")
	val deviceToken: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
