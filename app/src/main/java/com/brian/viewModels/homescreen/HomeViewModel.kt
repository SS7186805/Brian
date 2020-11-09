package com.brian.viewModels.homescreen

import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.internals.plusAssign
import com.brian.internals.toArrayList
import com.brian.models.*
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.homeRepository.HomeRepository
import com.brian.views.adapters.LeaderboardChallengeAdapter
import com.brian.views.adapters.LeaderboardPlayerAdapter
import com.brian.views.adapters.MyTeamMembersAdapter
import com.brian.views.adapters.MyTeamsAdapter

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {

    var list = ArrayList<DataItem>()
    var data = MutableLiveData<QuestionData>()
    var homeData = MutableLiveData<ArrayList<DataManagementDataItem>>()
    var questionsId = 0


    lateinit var myChallengesAdapter: LeaderboardChallengeAdapter
    lateinit var playersAdapter: LeaderboardPlayerAdapter
    lateinit var myTeamsAdapter: MyTeamsAdapter
    lateinit var teamPlayersAdapter: MyTeamMembersAdapter
    var challenges =
        MutableLiveData<ArrayList<LeaderboardDataItem>>().apply { value = ArrayList() }
    var players =
        MutableLiveData<ArrayList<LeaderboardDataItem>>().apply { value = ArrayList() }

    var myTeams =
        MutableLiveData<ArrayList<DataItemMyTeam>>().apply { value = ArrayList() }

    var myStats =
        MutableLiveData<DataMyStats>()

    var createTeamParams = CreateTeamParams()
    var submitAnswerParams = SubmitAnswerParams()
    var gameSummaryResponse = MutableLiveData<ResponseGameSummary>()
    var selectDefensiveId = 0
    var currentPageTeams = 1


    init {
        initRecyclerAdapters()
    }


    private fun initRecyclerAdapters() {
        myChallengesAdapter = LeaderboardChallengeAdapter(
            R.layout.leaderboard, resourcesProvider

        )
        playersAdapter = LeaderboardPlayerAdapter(
            R.layout.leaderboard, resourcesProvider

        )

        myTeamsAdapter = MyTeamsAdapter(
            R.layout.teams, resourcesProvider

        )

        teamPlayersAdapter = MyTeamMembersAdapter(
            R.layout.team_players_item, resourcesProvider

        )

    }


    fun getSelectDefensive() {
        homeRepository.getSelectDefensive(submitAnswerParams)
        { isSuccess, message, response ->
            if (isSuccess) {
                selectDefensiveId = response?.data?.id!!

            } else {

            }
        }
    }


    fun getDefensive() {
        homeRepository.getDefensive()
        { isSuccess, message, response ->
            if (isSuccess) {
                list = response?.data?.data!!.toArrayList()
            } else {

            }
        }
    }

    fun submitAnswer() {
        homeRepository.submitAnswer(submitAnswerParams)
        { isSuccess, message, response ->
            if (isSuccess) {

            } else {

            }
        }
    }

    fun questionRespone() {
        showLoading.postValue(true)
        homeRepository.questionResponse(questionsId) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                data.postValue(response?.data!!)
            } else {
                showMessage.postValue(message)

            }

        }
    }

    fun getAllChallenges() {
        showLoading.postValue(true)
        homeRepository.getChallenges() { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                challenges.postValue(response?.data)

            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun getAllPlayers() {
        showLoading.postValue(true)
        homeRepository.getPlayers() { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {

                players.postValue(response?.data)

            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun getMyStats() {
        showLoading.postValue(true)
        homeRepository.getMyStats() { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {

                myStats.postValue(response?.data)

            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun createTeam() {
        showLoading.postValue(true)
        homeRepository.createTeam(createTeamParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                showMessage.postValue(response?.result)

            } else {
                showMessage.postValue(message)
            }

        }
    }


    fun getMyTeams() {
        myTeams.value?.clear()
        showLoading.postValue(true)
        homeRepository.getMyTeams(currentPageTeams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                currentPageTeams= response?.data?.currentPage!! +1
                myTeams += response?.data?.data!!

            } else {
                showMessage.postValue(message)
            }

        }
    }


    fun getData() {
        showLoading.postValue(true)
        homeRepository.getData() { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                homeData.postValue(response?.data!!)

            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun getGameSummary() {
        showLoading.postValue(true)
        homeRepository.getGameSummary(selectDefensiveId) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                gameSummaryResponse.postValue(response)

            } else {
                showMessage.postValue(message)
            }

        }
    }


}