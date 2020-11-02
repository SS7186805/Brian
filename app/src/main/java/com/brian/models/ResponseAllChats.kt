package com.brian.models

import com.google.gson.annotations.SerializedName

data class ResponseAllChats(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("data")
	val data: DataAllChats? = null,

	@field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("error")
    var error: String? = null
)

data class DataAllChats(

    @field:SerializedName("first_page_url")
    val firstPageUrl: String? = null,

    @field:SerializedName("path")
    val path: String? = null,

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("data")
    val data: ArrayList<AllChatsDataItem>? = null,

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

data class AllChatsDataItem(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("delete_by_receiver_user")
    val deleteByReceiverUser: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("last_message")
    val lastMessage: AllChatsLastMessage? = null,

    @field:SerializedName("other_user_detail")
    val otherUserDetail: OtherUserDetail? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @field:SerializedName("sender_user_id")
    val senderUserId: Int? = null,

    @field:SerializedName("receiver_user_id")
    val receiverUserId: Int? = null,

    @field:SerializedName("delete_by_sender_user")
    val deleteBySenderUser: Int? = null
)
data class AllChatsLastMessage(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("thumbnail_video")
    val thumbnailVideo: Any? = null,

    @field:SerializedName("file_name")
    val fileName: Any? = null,

    @field:SerializedName("chat_room_id")
    val chatRoomId: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("type_of_file")
    val typeOfFile: Any? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @field:SerializedName("sender_user_id")
    val senderUserId: Int? = null,

    @field:SerializedName("receiver_user_id")
    val receiverUserId: Int? = null
)