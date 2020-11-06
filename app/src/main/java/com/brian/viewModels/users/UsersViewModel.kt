package com.brian.viewModels.users


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.internals.plusAssign
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
    var currentPageAllUsers = 1
    var currentPageMyUsers = 1
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
                    usersAdapter.clearData()
                    selectFriendsAdapter.selectedUsers.clear()
                    selectFriendsAdapter.clearData()
                    currentPageAllUsers = 1
                    currentPageMyUsers = 1
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
        Log.e("CurrentPageAllUsersOut", currentPageAllUsers.toString())

        searchQuery.page = currentPageAllUsers
        usersRepository.searchUsers(searchQuery) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allUsersLoaded = response?.data?.data.isNullOrEmpty()

                currentPageAllUsers = response?.data?.currentPage!! + 1
                Log.e("CurrentPageAllUsers", currentPageAllUsers.toString())

                usersList += response?.data.data!!
            } else {
                usersList.postValue(ArrayList())
                showMessage.postValue(message)
            }

        }
    }


    fun getMyUsers() {
        showLoading.postValue(true)
        searchQuery.page = currentPageMyUsers
        usersRepository.searchMyUsers(searchQuery) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allUsersLoaded = response?.data?.data.isNullOrEmpty()
                currentPageMyUsers = response?.data?.currentPage!! + 1
                myFriends += response?.data.data!!
            } else {
                myFriends.postValue(ArrayList())
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
        sendRequestParams.receiver_user_id = usersList.value!![position].senderId.toString()
        sendRequestParams.action = action
        usersRepository.acceptRejectRequest(sendRequestParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {

                if (action.contains(resourcesProvider.getString(R.string.accept))) {
                    usersAdapter.list[position].isAccepted =
                        resourcesProvider.getString(R.string.yes)
                    usersAdapter.notifyItemChanged(position)


                } else {
                    usersAdapter.list.removeAt(position)
                    usersAdapter.notifyItemRemoved(position)

                }

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
                if (action.contains(resourcesProvider.getString(R.string.accept))) {
                    myFriendsAdapter.list[position].isAccepted =
                        resourcesProvider.getString(R.string.yes)
                    myFriendsAdapter.notifyItemChanged(position)


                } else {
                    myFriendsAdapter.list.removeAt(position)
                    myFriendsAdapter.notifyItemRemoved(position)

                }


            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun removeFriend(position: Int) {
        showLoading.postValue(true)
        sendRequestParams.receiver_user_id = usersList.value!![position].id.toString()
        usersRepository.removeFriend(sendRequestParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                usersAdapter.list[position].reqSendByOther =
                    resourcesProvider.getString(R.string.No)
                usersAdapter.list[position].reqSendBySelf = resourcesProvider.getString(R.string.No)
                usersAdapter.list[position].isAccepted = resourcesProvider.getString(R.string.No)
                usersAdapter.notifyItemChanged(position)


            } else {
                showMessage.postValue(message)
            }

        }
    }


    fun getMyFriends() {
        showLoading.postValue(true)
        usersRepository.getMyFriends(currentPageMyFriends) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allFriendsLoaded = response?.data?.data.isNullOrEmpty()
                currentPageMyFriends = response?.data?.currentPage!! + 1
                myFriends.postValue(response?.data.data)

            } else {
                showMessage.postValue(message)
            }

        }
    }


}