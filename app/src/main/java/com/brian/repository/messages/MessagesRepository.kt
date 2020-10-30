package com.brian.repository.messages

import com.brian.models.ResponseAllChats
import com.brian.models.ResponseMyFriends
import com.brian.models.SearchQuery

interface MessagesRepository {

    fun getMessages(
        onResult: (
            isSuccess: Boolean,
            message: String,
            response: ResponseAllChats?
        ) -> Unit
    )


}