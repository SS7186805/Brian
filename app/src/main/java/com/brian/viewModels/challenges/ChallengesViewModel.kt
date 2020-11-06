package com.brian.viewModels.challenges


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.base.Prefs
import com.brian.internals.plusAssign
import com.brian.models.*
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.challenges.ChallengesRepository
import com.brian.repository.myProfileRepository.MyProfileRepository
import com.brian.views.adapters.ChallengeTypeAdapter
import com.brian.views.adapters.MyChallengesAdapter
import com.brian.views.adapters.MyChallengesRequestsAdapter


class ChallengesViewModel(
    private val challengesRepository: ChallengesRepository,
    private val profileRepository: MyProfileRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {


    var allChallenges =
        MutableLiveData<ArrayList<ChallengeTypeDataItem>>().apply { value = ArrayList() }
    var myChallenges =
        MutableLiveData<ArrayList<DataItemMyChalleneges>>().apply { value = ArrayList() }
    var loginDataa = MutableLiveData<LoginData>()

    var cancelResponse = MutableLiveData<BaseResponse>()
    var approveRejectResponse = MutableLiveData<BaseResponse>()
    var rejectRequestResponse = MutableLiveData<BaseResponse>()

    var approvedMyChallenges =
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
    var currentPageMyChalleneges = 1
    var currentPageChallenegesRequests = 1
    var currentPageAllChalleneges = 1
    var context: Context? = null

    init {
        initRecyclerAdapters()
    }

    fun viewProfile(id: String) {
        showLoading.postValue(true)
        profileRepository.viewProfile(id) { isSuccess, message, response ->
            showLoading.postValue(false)

            if (isSuccess) {

                if (response?.data is LoginData) {
                    val loginData: LoginData = response?.data
                    Prefs.init().userInfo = loginData
                    loginDataa.postValue(response.data)
                }
            } else {
                showMessage.postValue(message)
            }

        }
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
        challengesRepository.getChallenges(currentPageAllChalleneges) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                currentPageAllChalleneges = response?.data?.currentPage!! + 1
                allChallenges += response?.data.data!!

            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun getMyChallenges() {
        showLoading.postValue(true)
        challengesRepository.getMyChallenges(currentPageAllChalleneges) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                currentPageAllChalleneges = response?.data?.currentPage!! + 1
                myChallenges += response?.data.data!!

            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun getApprovedMyChallenges() {
        showLoading.postValue(true)
        challengesRepository.getMyChallenges(currentPageAllChalleneges) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                currentPageAllChalleneges = response?.data?.currentPage!! + 1

                var approvedList =
                    response.data.data?.filter { s -> (s.isApproved == 1 && s.isAccepted == 1) }
                myChallenges += approvedList!!

            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun getChallengesRequests() {
        showLoading.postValue(true)
        challengesRepository.getChallengesRequests(currentPageChallenegesRequests) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                currentPageChallenegesRequests = response?.data?.currentPage!! + 1
                challengeRequests += response?.data.data!!

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
                showMessage.postValue(message)
                rejectRequestResponse.postValue(response)

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
                approveRejectResponse.postValue(response)


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
                cancelResponse.postValue(response)

            } else {
                showMessage.postValue(message)
            }

        }
    }


}