package com.brian.viewModels.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brian.repository.authRepository.AuthenticationRepository

class LoginViewModelFactory(private val authenticationRepository: AuthenticationRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(authenticationRepository) as T
    }
}