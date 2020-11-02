package com.brian.viewModels.users


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.models.MyFriendsDataItem
import com.brian.models.SearchQuery
import com.brian.models.SendRequestParams
import com.brian.models.UserDataItem
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.usersRepositary.UsersRepository
import com.brian.views.adapters.AllUsersAdapter
import com.brian.views.adapters.MyFriendsAdapter
import com.brian.views.adapters.SelectFriendsAdapter


class UsersViewModel(
    private val usersRepository: UsersRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {


    lateinit var textWatcher: TextWatcher
    private var handler: Handler = Handler(Looper.getMainLooper() /*UI thread*/)
    private var workRunnable: Runnable? = null

    var usersList =
        MutableLiveData<ArrayList<UserDataItem>>().apply { value = ArrayList() }
    var myFriends =
        MutableLiveData<ArrayList<MyFriendsDataItem>>().apply { value = ArrayList() }
    lateinit var usersAdapter: AllUsersAdapter
    lateinit var selectFriendsAdapter: SelectFriendsAdapter
    lateinit var myFriendsAdapter: MyFriendsAdapter
    var searchQuery = SearchQuery()
    var sendRequestParams = SendRequestParams()
    var allFriendsLoaded = false
    var allUsersLoaded = false
    var currentPage = 1
    var currentPageMyFriends = 1
    var type = ""
    var context: Context? = null

    init {
        initRecyclerAdapters()
        initTextWatcher()
    }

    private fun initTextWatcher() {
        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                workRunnable?.let { handler.removeCallbacks(it) }
                workRunnable = Runnable {
                    usersList.postValue(ArrayList())
                    myFriends.postValue(ArrayList())
                    currentPage = 1
                    searchQuery.search_name = s.toString()
                    if (type.equals(resourcesProvider.getString(R.string.yes))) {
                        getMyUsers()
                    } else {
                        getUsers()

                    }
                }
                handler.postDelayed(workRunnable!!, 500 /*delay*/)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun initRecyclerAdapters() {
        usersAdapter = AllUsersAdapter(
            R.layout.users_item, resourcesProvider

        )
        selectFriendsAdapter = SelectFriendsAdapter(
            R.layout.my_friends_item, resourcesProvider

        )

        myFriendsAdapter = MyFriendsAdapter(
            R.layout.my_friends_item, resourcesProvider

        )

    }

    fun getUsers() {
        showLoading.postValue(true)
        usersRepository.searchUsers(searchQuery) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allUsersLoaded = response?.data?.data.isNullOrEmpty()
                currentPage = response?.data?.currentPage!!
                usersList.postValue(response?.data.data)
            } else {
                usersList.postValue(ArrayList())
                showMessage.postValue(message)
            }

        }
    }


    fun getMyUsers() {
        showLoading.postValue(true)
        usersRepository.searchMyUsers(searchQuery) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allUsersLoaded = response?.data?.data.isNullOrEmpty()
                currentPage = response?.data?.currentPage!!
                myFriends.postValue(response?.data.data)
            } else {
                usersList.postValue(ArrayList())
                showMessage.postValue(message)
            }

        }
    }

    fun sendRequest(position: Int) {
        showLoading.postValue(true)
        sendRequestParams.receiver_user_id = usersList.value!![position].id.toString()
        usersRepository.sendRequest(sendRequestParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                usersAdapter.list[position].reqSendBySelf =
                    resourcesProvider.getString(R.string.yes)
                usersAdapter.notifyItemChanged(position)


            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun cancelRequest(position: Int) {
        showLoading.postValue(true)
        sendRequestParams.receiver_user_id = usersList.value!![position].id.toString()
        usersRepository.cancelRequest(sendRequestParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                usersAdapter.list[position].reqSendBySelf =
                    resourcesProvider.getString(R.string.No)
                usersAdapter.notifyItemChanged(position)


            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun acceptRejectRequest(position: Int, action: String) {
        showLoading.postValue(true)
        sendRequestParams.receiver_user_id = usersList.value!![position].id.toString()
        sendRequestParams.action = action
        usersRepository.cancelRequest(sendRequestParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                usersAdapter.list[position].reqSendBySelf =
                    resourcesProvider.getString(R.string.No)
                usersAdapter.notifyItemChanged(position)


            } else {
                showMessage.postValue(message)
            }

        }
    }


    fun acceptRejectRequestFriends(position: Int, action: String) {
        showLoading.postValue(true)
        sendRequestParams.receiver_user_id = myFriends.value!![position].senderUserId.toString()
        sendRequestParams.action = action
        usersRepository.acceptRejectRequest(sendRequestParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                myFriendsAdapter.list[position].isAccepted =
                    if (action.contains(resourcesProvider.getString(R.string.accept))) resourcesProvider.getString(
                        R.string.yes
                    ) else resourcesProvider.getString(R.string.No)
                myFriendsAdapter.notifyItemChanged(position)


            } else {
                showMessage.postValue(message)
            }

        }
    }


    fun getMyFriends() {
        showLoading.postValue(true)
        usersRepository.getMyFriends() { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allFriendsLoaded = response?.data?.data.isNullOrEmpty()
                currentPageMyFriends = response?.data?.currentPage!!
                myFriends.postValue(response?.data.data)

            } else {
                showMessage.postValue(message)
            }

        }
    }


}