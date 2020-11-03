package com.brian.viewModels.trainingVideos


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.brian.R
import com.brian.base.BaseViewModel
import com.brian.internals.plusAssign
import com.brian.models.DataManagementDataItem
import com.brian.models.QueryParams
import com.brian.models.TrainingVideosCategoryDataItem
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepository
import com.brian.views.adapters.TrainingVideosAdapter


class TrainingViewModel(
    private val trainingVideosRepository: TrainingAndBuzzRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {


    var videoslist =
        MutableLiveData<ArrayList<TrainingVideosCategoryDataItem>>().apply { value = ArrayList() }
    var data = MutableLiveData<ArrayList<DataManagementDataItem>>()
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

    }

    fun getVideos() {
        showLoading.postValue(true)
        queryParams.page = currentPage
        trainingVideosRepository.getTrainingVideos(queryParams) { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                currentPage = response?.data?.trainingVideos?.currentPage!! + 1

                Log.e("videolistsuzebefor", videoslist.value!!.size.toString())
                videoslist += response.data.trainingVideos.data!!
                Log.e("videolistsuzafter", videoslist.value!!.size.toString())

            } else {
                showMessage.postValue(message)
            }

        }
    }

    fun getTrainingVideoDataManagement() {
        showLoading.postValue(true)
        trainingVideosRepository.getTrainingVideosDataManagement() { isSuccess, message, response ->
            showLoading.postValue(false)
            if (isSuccess) {
                data.postValue(response?.data!!)

            } else {
                showMessage.postValue(message)
            }

        }
    }


}