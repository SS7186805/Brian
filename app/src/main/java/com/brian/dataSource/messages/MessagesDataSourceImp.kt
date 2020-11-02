package com.brian.dataSource.messages

import com.brian.models.*
import com.brian.network.APIService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class MessagesDataSourceImp(private val apiService: APIService) : MessagesDataSource {


    override suspend fun getAllChats(): ResponseAllChats {
        var response = ResponseAllChats()
        try {
            response = apiService.getAllChats()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun sendMessage(sendMessageParams: SendMessageParams): ResponseSendMessage {
        var response = ResponseSendMessage()
        try {
            var params = HashMap<String, RequestBody>()
            params["message"] =
                RequestBody.create("text/plain".toMediaTypeOrNull(), sendMessageParams.message!!)
            params["other_user_id"] = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                sendMessageParams.other_user_id.toString()!!
            )
            params["chat_room_id"] = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                sendMessageParams.chat_room_id.toString()!!
            )
            response = apiService.sendMessage(params, null)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun getAllMessages(sendMessageParams: GetAllMessagesParams): ResponseGetAllMessages {
        var response = ResponseGetAllMessages()
        try {
            response = apiService.getAllMessages(sendMessageParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }

    override suspend fun createChatRoom(sendMessageParams: CreateChatRoomParams): ResponseCreateChatRoom {
        var response = ResponseCreateChatRoom()
        try {
            response = apiService.createChatRoom(sendMessageParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response
    }


}