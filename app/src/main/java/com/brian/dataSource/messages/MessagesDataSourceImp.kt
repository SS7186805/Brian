package com.brian.dataSource.messages

import com.brian.models.*
import com.brian.network.APIService

class MessagesDataSourceImp(private val apiService: APIService) : MessagesDataSource {


    override suspend fun getAllChats(): ResponseAllChats {
        var response = ResponseAllChats()
        try {
            response = apiService.getAllChats()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            response.error = APIService.getErrorMessageFromGenericResponse(e)
        }
        return response    }


}