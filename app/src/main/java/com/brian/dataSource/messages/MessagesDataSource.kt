package com.brian.dataSource.messages

import com.brian.models.*

interface MessagesDataSource {

    suspend fun getAllChats() : ResponseAllChats
    suspend fun sendMessage(sendMessageParams: SendMessageParams) : ResponseSendMessage
    suspend fun getAllMessages(sendMessageParams: GetAllMessagesParams) : ResponseGetAllMessages
    suspend fun createChatRoom(sendMessageParams: CreateChatRoomParams) : ResponseCreateChatRoom





}
