package com.brian.viewModels.trainingVideos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepository
import com.brian.viewModels.users.UsersViewModel

class TrainingsViewModelFactory(
    private val trainingVideosRepository: TrainingAndBuzzRepository, private val resourcesProvider: ResourcesProvider
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrainingViewModel(trainingVideosRepository,resourcesProvider) as T
    }
}