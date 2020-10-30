package com.brian.viewModels.messages


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.models.AllChatsDataItem
import com.brian.models.MyFriendsDataItem
import com.brian.models.SearchQuery
import com.brian.models.SendRequestParams
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.messages.MessagesRepository
import com.brian.views.adapters.AllUsersAdapter
import com.brian.views.adapters.MyFriendsAdapter
import com.brian.views.adapters.MyMessagesAdapter
import com.brian.views.adapters.SelectFriendsAdapter


class MessagesViewModel(
    private val messagesRepository: MessagesRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {


    var allChats =
        MutableLiveData<ArrayList<AllChatsDataItem>>().apply { value = ArrayList() }
    var myFriends =
        MutableLiveData<ArrayList<MyFriendsDataItem>>().apply { value = ArrayList() }
    lateinit var messagesAdapter: MyMessagesAdapter
    var allFriendsLoaded = false
    var allChatsLoaded= false
    var currentPageAllChats = 1


    init {
        initRecyclerAdapters()

    }

    private fun initRecyclerAdapters() {
        messagesAdapter = MyMessagesAdapter(
            R.layout.users_item, resourcesProvider

        )


    }

    fun getAllChats() {
        showLoading.postValue(true)
        messagesRepository.getMessages() { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allChatsLoaded = response?.data?.data.isNullOrEmpty()
                currentPageAllChats = response?.data?.currentPage!!
                allChats.postValue(response?.data.data)
            } else {
                showMessage.postValue(message)
            }

        }
    }




}