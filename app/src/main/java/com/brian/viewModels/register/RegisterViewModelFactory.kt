package com.brian.viewModels.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.authRepository.authRepository.AuthenticationRepository

class RegisterViewModelFactory(
    private val authenticationRepository: AuthenticationRepository, private val resourcesProvider: ResourcesProvider
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterViewModel(authenticationRepository,resourcesProvider) as T
    }
}