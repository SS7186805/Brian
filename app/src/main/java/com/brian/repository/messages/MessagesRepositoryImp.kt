package com.brian.repository.messages

import com.brian.dataSource.messages.MessagesDataSource
import com.brian.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessagesRepositoryImp(private val messagesDataSource: MessagesDataSource) :
    MessagesRepository {


    override fun getMessages( page:Int,onResult: (isSuccess: Boolean, message: String, response: ResponseAllChats?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = messagesDataSource.getAllChats(page)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun sendMessage(
        sendMessageParams: SendMessageParams,
        onResult: (isSuccess: Boolean, message: String, response: ResponseSendMessage?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = messagesDataSource.sendMessage(sendMessageParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun getAllMessages(
        sendMessageParams: GetAllMessagesParams,
        onResult: (isSuccess: Boolean, message: String, response: ResponseGetAllMessages?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = messagesDataSource.getAllMessages(sendMessageParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun creteChatRoom(
        sendMessageParams: CreateChatRoomParams,
        onResult: (isSuccess: Boolean, message: String, response: ResponseCreateChatRoom?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = messagesDataSource.createChatRoom(sendMessageParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun removeChat(
        sendMessageParams: GetAllMessagesParams,
        onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = messagesDataSource.removeChat(sendMessageParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }    }
}