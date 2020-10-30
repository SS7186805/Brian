package com.brian.viewModels.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brian.providers.resources.ResourcesProvider
import com.brian.repository.messages.MessagesRepository
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepository
import com.brian.repository.usersRepositary.UsersRepository

class MessagesViewModelFactory(
    private val messagesRepository: MessagesRepository, private val resourcesProvider: ResourcesProvider
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MessagesViewModel(messagesRepository,resourcesProvider) as T
    }
}