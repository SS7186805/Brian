package com.brian.viewModels.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.homeRepository.HomeRepository

class HomescreenViewModelFactory(private val homeRepository: HomeRepository,
                                 private val resourcesProvider: ResourcesProvider
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(homeRepository,resourcesProvider) as T
    }
}