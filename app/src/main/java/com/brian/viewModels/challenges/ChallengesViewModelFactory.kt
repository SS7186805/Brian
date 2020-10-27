package com.brian.viewModels.challenges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.challenges.ChallengesRepository
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepository
import com.brian.repository.usersRepositary.UsersRepository

class ChallengesViewModelFactory(
    private val challengesRepository: ChallengesRepository, private val resourcesProvider: ResourcesProvider
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChallengesViewModel(challengesRepository,resourcesProvider) as T
    }
}