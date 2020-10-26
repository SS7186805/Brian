package com.brian.viewModels.myProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brian.repository.myProfileRepository.MyProfileRepository

class MyProfileViewModelFactory(private val myProfileRepository: MyProfileRepository):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyProfileViewModel(myProfileRepository) as T
    }
}