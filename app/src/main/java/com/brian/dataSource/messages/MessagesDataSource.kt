package com.brian.dataSource.messages

import com.brian.models.*

interface MessagesDataSource {

    suspend fun getAllChats( page:Int) : ResponseAllChats
    suspend fun sendMessage(sendMessageParams: SendMessageParams) : ResponseSendMessage
    suspend fun getAllMessages(page: Int,sendMessageParams: GetAllMessagesParams) : ResponseGetAllMessages
    suspend fun createChatRoom(sendMessageParams: CreateChatRoomParams) : ResponseCreateChatRoom
    suspend fun removeChat(sendMessageParams: GetAllMessagesParams) : BaseResponse





}
