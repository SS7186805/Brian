package com.brian.repository.messages

import com.brian.dataSource.messages.MessagesDataSource
import com.brian.models.ResponseAllChats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessagesRepositoryImp(private val messagesDataSource: MessagesDataSource) :
    MessagesRepository {


    override fun getMessages(onResult: (isSuccess: Boolean, message: String, response: ResponseAllChats?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = messagesDataSource.getAllChats()
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }
}