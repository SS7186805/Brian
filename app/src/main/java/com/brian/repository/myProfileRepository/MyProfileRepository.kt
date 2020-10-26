package com.brian.repository.myProfileRepository

import com.brian.models.*

interface MyProfileRepository {
    fun changePassword(change:ChangePassword,onResult: (
        isSuccess: Boolean,
        message: String,
        response: BaseResponse?
    ) -> Unit)

    fun viewProfile(onResult: (
        isSuccess: Boolean,
        message: String,
        response: BaseResponse?
    ) -> Unit)



}