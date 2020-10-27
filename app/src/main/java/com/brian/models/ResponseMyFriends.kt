package com.brian.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponseMyFriends(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("data")
	val data: DataMyFriends? = null,

	@field:SerializedName("message")
	val message: String? = null,


	@field:SerializedName("error")
	var error: String? = null

)

data class DataMyFriends(

	@field:SerializedName("first_page_url")
	val firstPageUrl: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: ArrayList<MyFriendsDataItem>? = null,

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

@Parcelize
data class OtherUserDetail(

	@field:SerializedName("is_block")
	val isBlock: Int? = null,

	@field:SerializedName("is_verify")
	val isVerify: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("device_type")
	val deviceType: String? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: String? = null,

	@field:SerializedName("weekly_situation")
	val weeklySituation: String? = null,

	@field:SerializedName("weekly_complete_challenge")
	val weeklyCompleteChallenge: String? = null,

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("device_token")
	val deviceToken: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
):Parcelable

@Parcelize
data class MyFriendsDataItem(

	@field:SerializedName("is_accepted")
	var isAccepted: String? = null,

	@field:SerializedName("isSelected")
	var isSelected: Boolean = false,


	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("uname")
	val uname: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("other_user_detail")
	val otherUserDetail: OtherUserDetail? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: String? = null,

	@field:SerializedName("sender_user_id")
	val senderUserId: Int? = null,

	@field:SerializedName("receiver_user_id")
	val receiverUserId: Int? = null
):Parcelable