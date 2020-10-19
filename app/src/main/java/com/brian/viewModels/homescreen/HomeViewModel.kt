package com.brian.viewModels.homescreen

import androidx.lifecycle.MutableLiveData
import com.brian.base.BaseViewModel
import com.brian.internals.toArrayList
import com.brian.models.AnswersItem
import com.brian.models.DataItem
import com.brian.models.QuestionData
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.authRepository.homeRepository.HomeRepository

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val resourcesProvider: ResourcesProvider
) : BaseViewModel() {

    var list = ArrayList<DataItem>()
    var data = MutableLiveData<QuestionData>()

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

}