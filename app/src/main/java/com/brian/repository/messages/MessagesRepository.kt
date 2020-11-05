package com.brian.repository.messages

import com.brian.models.*

interface MessagesRepository {

    fun getMessages(
        page: Int,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseAllChats?
        ) -> Unit
    )

    fun sendMessage(
        sendMessageParams: SendMessageParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseSendMessage?
        ) -> Unit
    )

    fun getAllMessages(
        page: Int,
        sendMessageParams: GetAllMessagesParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseGetAllMessages?
        ) -> Unit
    )


    fun creteChatRoom(
        sendMessageParams: CreateChatRoomParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseCreateChatRoom?
        ) -> Unit
    )

    fun removeChat(
        sendMessageParams: GetAllMessagesParams,
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: BaseResponse?
        ) -> Unit
    )


}