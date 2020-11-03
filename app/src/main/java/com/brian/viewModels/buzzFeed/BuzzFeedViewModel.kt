package com.brian.viewModels.buzzFeed


import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.internals.plusAssign
import com.brian.models.BuzzFeedDataItem
import com.brian.models.QueryParams
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepository
import com.brian.views.adapters.BuzzFeedAdapter


class BuzzFeedViewModel(
    private val trainingVideosRepository: TrainingAndBuzzRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {


    var feedList =
        MutableLiveData<ArrayList<BuzzFeedDataItem>>().apply { value = ArrayList() }
    lateinit var buzzFeedAdapter: BuzzFeedAdapter
    var queryParams = QueryParams()
    var allVideosLoaded = false
    var currentPage = 1

    init {
        initRecyclerAdapters()
    }


    private fun initRecyclerAdapters() {
        buzzFeedAdapter = BuzzFeedAdapter(
            R.layout.buzz_feed_item
        )
        getBuzzFeed()

    }


    fun getBuzzFeed() {
        showLoading.postValue(true)
        queryParams.page = currentPage
        trainingVideosRepository.getBuzzFeed(queryParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allVideosLoaded = response?.data?.data.isNullOrEmpty()
                currentPage = response?.data?.currentPage!! +1
                feedList += response?.data?.data!!
            } else {
                showMessage.postValue(message)
            }

        }
    }
}