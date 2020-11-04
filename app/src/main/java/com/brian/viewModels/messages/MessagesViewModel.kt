package com.brian.viewModels.messages

import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.internals.plusAssign
import com.brian.models.*
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.messages.MessagesRepository
import com.brian.views.adapters.ChatAdapter
import com.brian.views.adapters.MyMessagesAdapter


class MessagesViewModel(
    private val messagesRepository: MessagesRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {


    var allChats =
        MutableLiveData<ArrayList<AllChatsDataItem>>().apply { value = ArrayList() }
    var allMessages =
        MutableLiveData<ArrayList<AllMessagesDataItem>>().apply { value = ArrayList() }
    var sendMessageResponse = MutableLiveData<ResponseSendMessage>()
    var getAllChatParams = GetAllMessagesParams()
    var removeChatParams = GetAllMessagesParams()
    var createChatRoomParams = CreateChatRoomParams()
    var resCreateChatRoomParams = MutableLiveData<ResponseCreateChatRoom>()
    var removeChat = MutableLiveData<BaseResponse>()

    lateinit var messagesAdapter: MyMessagesAdapter
    lateinit var chatAdapter: ChatAdapter
    var allFriendsLoaded = false
    var allChatsLoaded = false
    var currentPageAllChats = 1


    init {
        initRecyclerAdapters()

    }

    private fun initRecyclerAdapters() {
        messagesAdapter = MyMessagesAdapter(
            R.layout.messages, resourcesProvider

        )

        chatAdapter = ChatAdapter(
            resourcesProvider, allMessages.value!!
        )


    }

    fun getAllChats() {

        showLoading.postValue(true)
        messagesRepository.getMessages(currentPageAllChats) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allChatsLoaded = response?.data?.data.isNullOrEmpty()
                currentPageAllChats = response?.data?.currentPage!! + 1
                allChats += response?.data.data!!
            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun sendMessage(sendMessageParams: SendMessageParams) {
        showLoading.postValue(true)
        messagesRepository.sendMessage(sendMessageParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                sendMessageResponse.postValue(response)
            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun getAllMessages() {
        showLoading.postValue(true)
        messagesRepository.getAllMessages(getAllChatParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allMessages.postValue(response?.data?.data)
            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun createChatRoom() {
        showLoading.postValue(true)
        messagesRepository.creteChatRoom(createChatRoomParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                resCreateChatRoomParams.postValue(response)
            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun removeChat() {
        showLoading.postValue(true)
        messagesRepository.removeChat(removeChatParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                removeChat.postValue(response)

            } else {
                showMessage.postValue(message)
            }

        }


    }


}