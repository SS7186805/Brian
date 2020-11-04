package com.brian.repository.usersRepositary

import com.brian.dataSource.users.UsersDataSource
import com.brian.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UsersRepositoryImp(private val usersDataSource: UsersDataSource) :
    UsersRepository {


    override fun searchUsers(
        queryParams: SearchQuery,
        onResult: (isSuccess: Boolean, message: String, response: ResponseSearchUsers?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = usersDataSource.getSearchUsers(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun searchMyUsers(
        queryParams: SearchQuery,
        onResult: (isSuccess: Boolean, message: String, response: ResponseMyFriends?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = usersDataSource.getSearchMyUsers(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }    }

    override fun getMyFriends(page:Int,onResult: (isSuccess: Boolean, message: String, response: ResponseMyFriends?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = usersDataSource.getMyFriends(page)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun sendRequest(
        queryParams: SendRequestParams,
        onResult: (isSuccess: Boolean, message: String, response: ResponseSendRequest?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = usersDataSource.sendRequest(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun cancelRequest(
        queryParams: SendRequestParams,
        onResult: (isSuccess: Boolean, message: String, response: ResponseSendRequest?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = usersDataSource.cancelRequest(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun acceptRejectRequest(
        queryParams: SendRequestParams,
        onResult: (isSuccess: Boolean, message: String, response: ResponseSendRequest?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = usersDataSource.acceptRejectRequest(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }
    }

    override fun removeFriend(
        queryParams: SendRequestParams,
        onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val response = usersDataSource.removeFriend(queryParams)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }    }
}