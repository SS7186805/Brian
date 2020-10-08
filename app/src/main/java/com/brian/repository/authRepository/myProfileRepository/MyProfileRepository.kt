package com.brian.repository.authRepository.myProfileRepository

import com.brian.models.*

interface MyProfileRepository {
    fun changePassword(change:ChangePassword,onResult: (
        isSuccess: Boolean,
        message: String,
        response: BaseResponse?
    ) -> Unit)



}