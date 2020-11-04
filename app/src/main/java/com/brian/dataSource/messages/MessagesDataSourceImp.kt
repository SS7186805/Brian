package com.brian.dataSource.messages

import com.brian.models.*
import com.brian.network.APIService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class MessagesDataSourceImp(private val apiService: APIService) : MessagesDataSource {


    override suspend fun getAllChats( page:Int): ResponseAllChats {
        var response = ResponseAllChats()
        try {
            response = apiService.getAllChats(page)
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
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    sendMessageParams.message.toString()
                )
            params["other_user_id"] = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                sendMessageParams.other_user_id.toString()
            )
            params["chat_room_id"] = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                sendMessageParams.chat_room_id.toString()
            )

            if (sendMessageParams.file_name != null) {
                params["type_of_file"] = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    sendMessageParams.type_of_file.toString()
                )
            }


            response = apiService.sendMessage(params, sendMessageParams.file_name)
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

    override suspend fun removeChat(sendMessageParams: GetAllMessagesParams): BaseResponse {
        var response = BaseResponse()
        try {
            response = apiService.removeChat(sendMessageParams)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response    }


}