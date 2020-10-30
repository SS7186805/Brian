package com.brian.dataSource.myProfileDataSource

import com.brian.models.*

interface MyProfileDataSource {

    suspend fun changePassword(change: ChangePassword) : BaseResponse

    suspend fun viewProfile(id:String) : BaseResponse




}
