package com.brian.network.dataSource.myProfileDataSource

import com.brian.models.*

interface MyProfileDataSource {

    suspend fun changePassword(change: ChangePassword) : BaseResponse

    suspend fun viewProfile() : BaseResponse




}
