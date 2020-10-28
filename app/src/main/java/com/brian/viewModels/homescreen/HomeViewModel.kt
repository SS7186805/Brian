package com.brian.viewModels.homescreen

import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.internals.toArrayList
import com.brian.models.DataItem
import com.brian.models.DataMyStats
import com.brian.models.LeaderboardDataItem
import com.brian.models.QuestionData
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.homeRepository.HomeRepository
import com.brian.views.adapters.LeaderboardChallengeAdapter

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {

    var list = ArrayList<DataItem>()
    var data = MutableLiveData<QuestionData>()

    lateinit var myChallengesAdapter: LeaderboardChallengeAdapter
    lateinit var playersAdapter: LeaderboardChallengeAdapter
    var challenges =
        MutableLiveData<ArrayList<LeaderboardDataItem>>().apply { value = ArrayList() }
    var players =
        MutableLiveData<ArrayList<LeaderboardDataItem>>().apply { value = ArrayList() }

    var myStats =
        MutableLiveData<DataMyStats>()

    init {
        initRecyclerAdapters()
    }


    private fun initRecyclerAdapters() {
        myChallengesAdapter = LeaderboardChallengeAdapter(
            R.layout.leaderboard, resourcesProvider

        )
        playersAdapter = LeaderboardChallengeAdapter(
            R.layout.leaderboard, resourcesProvider

        )

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

    fun questionRespone() {
        showLoading.postValue(true)
        homeRepository.questionResponse() { isSuccess, message, response ->
            if (isSuccess) {
                data.postValue(response?.data!!)
                showLoading.postValue(false)
            } else {

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
        homeRepository.getMyStats() { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {

                myStats.postValue(response?.data)

            } else {
                showMessage.postValue(message)
            }

        }
    }


}