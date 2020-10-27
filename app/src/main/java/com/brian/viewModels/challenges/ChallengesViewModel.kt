package com.brian.viewModels.challenges


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.models.ChallengeTypeDataItem
import com.brian.models.CreateChallengeParams
import com.brian.models.ResponseCreateChallenge
import com.brian.models.UserDataItem
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.challenges.ChallengesRepository
import com.brian.views.adapters.ChallengeTypeAdapter
import com.brian.views.adapters.MyFriendsAdapter


class ChallengesViewModel(
    private val challengesRepository: ChallengesRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {


    var usersList =
        MutableLiveData<ArrayList<UserDataItem>>().apply { value = ArrayList() }
    var allChallenges =
        MutableLiveData<ArrayList<ChallengeTypeDataItem>>().apply { value = ArrayList() }
    var createChallengeResponse =
        MutableLiveData<ResponseCreateChallenge>()


    lateinit var allChallengesAdapter: ChallengeTypeAdapter
    lateinit var myFriendsAdapter: MyFriendsAdapter
    var createChallengeParams = CreateChallengeParams()
    var allFriendsLoaded = false
    var allChallengesLoaded = false
    var currentPage = 1
    var currentPageAllChalleneges = 1
    var context: Context? = null

    init {
        initRecyclerAdapters()
    }


    private fun initRecyclerAdapters() {
        allChallengesAdapter = ChallengeTypeAdapter(
            R.layout.challenge_type, resourcesProvider

        )
        myFriendsAdapter = MyFriendsAdapter(
            R.layout.my_friends_item, resourcesProvider

        )

    }

    fun createChallenge() {
        showLoading.postValue(true)
        challengesRepository.createChallenge(createChallengeParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                createChallengeResponse.postValue(response)
            } else {
                showMessage.postValue(message)
            }

        }
    }


    fun getAllChallenges() {
        showLoading.postValue(true)
        challengesRepository.getChallenges() { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allChallengesLoaded = response?.data?.data.isNullOrEmpty()
                currentPageAllChalleneges = response?.data?.currentPage!!
                allChallenges.postValue(response?.data.data)

            } else {
                showMessage.postValue(message)
            }

        }
    }


}