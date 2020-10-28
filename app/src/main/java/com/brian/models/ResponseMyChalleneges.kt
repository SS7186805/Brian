package com.brian.models

import com.google.gson.annotations.SerializedName

data class ResponseMyChallenges(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("data")
	val data: ChallengesData? = null,

	@field:SerializedName("message")
	val message: String? = null,


	@field:SerializedName("error")
	var error: String? = null
)

data class OtherUserDetails(

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
	val weeklySituation: Any? = null,

	@field:SerializedName("weekly_complete_challenge")
	val weeklyCompleteChallenge: Any? = null,

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

data class Challenge(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("challenge_name")
	val challengeName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null
)

data class ChallengesData(

	@field:SerializedName("first_page_url")
	val firstPageUrl: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: ArrayList<DataItemMyChalleneges>? = null,

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

data class DataItemMyChalleneges(

	@field:SerializedName("is_accepted")
	val isAccepted: Int? = null,

	@field:SerializedName("challenge_id")
	val challengeId: Int? = null,

	@field:SerializedName("other_user_details")
	val otherUserDetails: OtherUserDetails? = null,

	@field:SerializedName("file_name")
	val fileName: Any? = null,

	@field:SerializedName("challenge_to_user_id")
	val challengeToUserId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("type_of_file")
	val typeOfFile: Any? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("challenge_from_user_id")
	val challengeFromUserId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("date_and_time")
	val dateAndTime: String? = null,

	@field:SerializedName("thumbnail_video")
	val thumbnailVideo: Any? = null,

	@field:SerializedName("is_approved")
	val isApproved: Int? = null,

	@field:SerializedName("challenge")
	val challenge: Challenge? = null,

	@field:SerializedName("challenge_title")
	val challengeTitle: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)