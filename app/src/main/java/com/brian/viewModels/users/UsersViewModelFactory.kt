package com.brian.viewModels.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepository
import com.brian.repository.usersRepositary.UsersRepository

class UsersViewModelFactory(
    private val usersRepository: UsersRepository, private val resourcesProvider: ResourcesProvider
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UsersViewModel(usersRepository,resourcesProvider) as T
    }
}