package com.brian.viewModels.trainingVideos


import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.models.QueryParams
import com.brian.models.TrainingVideosDataItem
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepository
import com.brian.views.adapters.TrainingVideosAdapter


class TrainingViewModel(
    private val trainingVideosRepository: TrainingAndBuzzRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {


    var videoslist =
        MutableLiveData<ArrayList<TrainingVideosDataItem>>().apply { value = ArrayList() }
    lateinit var trainingVideosAdapter: TrainingVideosAdapter
    var queryParams = QueryParams()
    var allVideosLoaded = false
    var currentPage = 1

    init {
        initRecyclerAdapters()
    }


    private fun initRecyclerAdapters() {
        trainingVideosAdapter = TrainingVideosAdapter(
            R.layout.training_videos_item
        )
        getVideos()

    }

    fun getVideos() {
        showLoading.postValue(true)
        queryParams.page = currentPage
        trainingVideosRepository.getTrainingVideos(queryParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                allVideosLoaded = response?.data?.data.isNullOrEmpty()
                currentPage = response?.data?.currentPage!!
                videoslist.postValue(response?.data?.data)
            } else {
                showMessage.postValue(message)
            }

        }
    }


}