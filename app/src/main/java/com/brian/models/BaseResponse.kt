package com.brian.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class BaseResponse(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("data")
	val data: @RawValue Any? = null,

	@field:SerializedName("error")
	var error: Error? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable


@Parcelize
data class Error(
	@field:SerializedName("status")
	var status: Int? = 1,

	@field:SerializedName("message")
	var message: String? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("is_block")
	val isBlock: Int? = null,

	@field:SerializedName("is_verify")
	val isVerify: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: @RawValue Any? = null,

	@field:SerializedName("device_type")
	val deviceType: String? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: @RawValue Any? = null,

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("refresh_token")
	val refreshToken: String? = null,

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
) : Parcelable
