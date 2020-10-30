package com.brian.repository.myProfileRepository

import com.brian.models.*
import com.brian.dataSource.myProfileDataSource.MyProfileDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyProfileRepositoryImp(private val myProfileDataSource: MyProfileDataSource):MyProfileRepository {
    override fun changePassword(change:ChangePassword, onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main){
            val response = myProfileDataSource.changePassword(change)
            if (response.error != null) {
                onResult(false, response.error!!, null)
            } else {
                onResult(true, response.message!!, response)
            }
        }

    }

    override fun viewProfile(id:String,onResult: (isSuccess: Boolean, message: String, response: BaseResponse?) -> Unit) {
        GlobalScope.launch ( Dispatchers.Main){
            val response = myProfileDataSource.viewProfile(id)
            if (response.error != null){
                onResult(false,response.error!!,null)
            }else{
                onResult(true,response.message!!,response)
            }
        }
    }
}