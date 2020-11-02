package com.brian.models

import com.google.gson.annotations.SerializedName

data class ResponseCreateChatRoom(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("data")
	val data: CreateChatRoomData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	var error: String? = null
)

data class CreateChatRoomData(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("delete_by_receiver_user")
	val deleteByReceiverUser: Int? = null,

	@field:SerializedName("chats")
	val chats: List<CreateRoomChatsItem?>? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

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

data class CreateRoomChatsItem(

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
	val typeOfFile: String? = null,

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