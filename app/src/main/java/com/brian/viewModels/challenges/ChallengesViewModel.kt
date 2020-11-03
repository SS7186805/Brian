package com.brian.viewModels.challenges


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.models.*
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.challenges.ChallengesRepository
import com.brian.views.adapters.ChallengeTypeAdapter
import com.brian.views.adapters.MyChallengesAdapter
import com.brian.views.adapters.MyChallengesRequestsAdapter


class ChallengesViewModel(
    private val challengesRepository: ChallengesRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {


    var allChallenges =
        MutableLiveData<ArrayList<ChallengeTypeDataItem>>().apply { value = ArrayList() }
    var myChallenges =
        MutableLiveData<ArrayList<DataItemMyChalleneges>>().apply { value = ArrayList() }
    var challengeRequests =
        MutableLiveData<ArrayList<DataItemMyChalleneges>>().apply { value = ArrayList() }

    var createChallengeResponse =
        MutableLiveData<ResponseCreateChallenge>()


    lateinit var allChallengesAdapter: ChallengeTypeAdapter
    lateinit var myChallengesAdapter: MyChallengesAdapter
    lateinit var challengeRequestsAdapter: MyChallengesRequestsAdapter
    var createChallengeParams = CreateChallengeParams()
    var rejectChallengeRequestParams = CreateChatRoomParams()
    var acceptChallengeRequestParams = AcceptChallengeParams()
    var approveRejectMyChallengeRequestParams = AcceptChallengeParams()
    var myChallengesLoaded = false
    var challengeRequestsLoaded = false
    var allChallengesLoaded = false
    var currentPageMyChalleneges = 1
    var currentPageChallenegesRequests = 1
    var currentPageAllChalleneges = 1
    var context: Context? = null

    init {
        initRecyclerAdapters()
    }


    private fun initRecyclerAdapters() {
        allChallengesAdapter = ChallengeTypeAdapter(
            R.layout.challenge_type, resourcesProvider

        )
        myChallengesAdapter = MyChallengesAdapter(
            R.layout.my_challenges_item, resourcesProvider

        )

        challengeRequestsAdapter = MyChallengesRequestsAdapter(
            R.layout.my_challenges_item, resourcesProvider

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

    fun getMyChallenges() {
        showLoading.postValue(true)
        challengesRepository.getMyChallenges() { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allChallengesLoaded = response?.data?.data.isNullOrEmpty()
                currentPageAllChalleneges = response?.data?.currentPage!!
                myChallenges.postValue(response?.data.data)

            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun getChallengesRequests() {
        showLoading.postValue(true)
        challengesRepository.getChallengesRequests() { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allChallengesLoaded = response?.data?.data.isNullOrEmpty()
                currentPageAllChalleneges = response?.data?.currentPage!!
                challengeRequests.postValue(response?.data.data)

            } else {
                showMessage.postValue(message)
            }

        }
    }


    fun acceptChallengeRequests() {
        showLoading.postValue(true)
        challengesRepository.acceptRejectChallengeRequests(acceptChallengeRequestParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {


            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun approveRejectChallengeRequests() {
        showLoading.postValue(true)
        challengesRepository.approveRejectMyChallenge(approveRejectMyChallengeRequestParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {


            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun cancelMyChallengeRequests() {
        showLoading.postValue(true)
        challengesRepository.cancelMyChallengesRequests(rejectChallengeRequestParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {

            } else {
                showMessage.postValue(message)
            }

        }
    }


}