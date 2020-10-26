package com.brian.viewModels.trainingVideos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepository

class BuzzFeedViewModelFactory(
    private val trainingVideosRepository: TrainingAndBuzzRepository, private val resourcesProvider: ResourcesProvider
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BuzzFeedViewModel(trainingVideosRepository,resourcesProvider) as T
    }
}