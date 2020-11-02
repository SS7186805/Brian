package com.brian.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import okhttp3.MultipartBody


@Parcelize
data class BaseResponse(

    @field:SerializedName("result")
    val result: String? = null,

    @field:SerializedName("data")
    val data: @RawValue LoginData? = null,

    @field:SerializedName("error")
    var error: String? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable


@Parcelize
data class LoginData(

    @field:SerializedName("is_block")
    val isBlock: Int? = null,

    @field:SerializedName("is_verify")
    val isVerify: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("profile_picture")
    var profilePicture: @RawValue Any? = null,

    @field:SerializedName("device_type")
    val deviceType: String? = null,

    @field:SerializedName("badges_earneda")
    val badgesEarneda: BadgesEarneda? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: @RawValue Any? = null,

    @field:SerializedName("weekly_situation")
    val weeklySituation: @RawValue Any? = null,

    @field:SerializedName("weekly_complete_challenge")
    val weeklyCompleteChallenge: @RawValue Any? = null,

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
    var name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null

) : Parcelable

@Parcelize
data class BadgesEarneda(

    @field:SerializedName("first_page_url")
    val firstPageUrl: String? = null,

    @field:SerializedName("path")
    val path: String? = null,

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("data")
    val data: @RawValue List<Any?>? = null,

    @field:SerializedName("last_page")
    val lastPage: Int? = null,

    @field:SerializedName("last_page_url")
    val lastPageUrl: String? = null,

    @field:SerializedName("next_page_url")
    val nextPageUrl: @RawValue Any? = null,

    @field:SerializedName("from")
    val from: @RawValue Any? = null,

    @field:SerializedName("to")
    val to: @RawValue Any? = null,

    @field:SerializedName("prev_page_url")
    val prevPageUrl: @RawValue Any? = null,

    @field:SerializedName("current_page")
    val currentPage: Int? = null
) : Parcelable


class QueryParams {
    @field:SerializedName("page")
    var page: Int? = null

    @field:SerializedName("limit")
    var limit: Int? = null
}


class SearchQuery {
    @field:SerializedName("search_name")
    var search_name: String = ""

}

class SendRequestParams {
    @field:SerializedName("receiver_user_id")

    var receiver_user_id: String = ""
    @field:SerializedName("action")
    var action: String? = null

}

class ContactUsParams {
    @field:SerializedName("title")
    var title: String = ""

    @field:SerializedName("description")
    var description: String? = null

}

data class CreateChallengeParams(
    @field:SerializedName("challenge_id")
    var challenge_id: Int? = null,

    @field:SerializedName("challenge_to_user")
    var challenge_to_user: String? = null,

    @field:SerializedName("challenge_title")
    var challenge_title: String? = null,

    @field:SerializedName("date_and_time")
    var date_and_time: String? = null

)

data class CreateTeamParams(
    @field:SerializedName("team_name")
    var team_name: String? = null,

    @field:SerializedName("users")
    var users: String? = null,

    @field:SerializedName("image")
    var image: MultipartBody.Part? = null


)

data class SendMessageParams(

    @field:SerializedName("other_user_id")
    var other_user_id: Int? = null,

    @field:SerializedName("chat_room_id")
    var chat_room_id: Int? = null,

    @field:SerializedName("message")
    var message: String? = null,


    @field:SerializedName("type_of_file")
    var type_of_file: String? = null,

    @field:SerializedName("file_name")
    var file_name: MultipartBody.Part? = null


)

class GetAllMessagesParams {
    @field:SerializedName("chat_room_id")
    var chat_room_id: Int? = null

}

class CreateChatRoomParams {
    @field:SerializedName("other_user_id")
    var other_user_id: Int? = null
    @field:SerializedName("user_challenge_id")
    var user_challenge_id: Int? = null

}

class AcceptChallengeParams {
    @field:SerializedName("user_challenge_id")
    var user_challenge_id: Int? = null

    @field:SerializedName("status")
    var status: Int? = null

    @field:SerializedName("file_name")
    var file_name: MultipartBody.Part? = null


}






