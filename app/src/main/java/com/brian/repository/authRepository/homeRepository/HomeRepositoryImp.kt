package com.brian.repository.authRepository.homeRepository

import com.brian.models.DefensiveResponse
import com.brian.models.QuestionResponse
import com.brian.network.dataSource.homeFragmentDataSource.HomeDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeRepositoryImp(private val homeDataSource: HomeDataSource) : HomeRepository {

    override fun getDefensive(onResult: (isSuccess: Boolean, message: String, response: DefensiveResponse?) -> Unit) {

            GlobalScope.launch(Dispatchers.Main) {
                val response = homeDataSource.getDefensive()
                if (response.error != null) {
                    onResult(false, response.error!!.message!!, null)
                } else {
                    onResult(true, response.message!!, response)
                }
            }

        }

    override fun questionResponse(onResult: (isSuccess: Boolean, message: String, response: QuestionResponse?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main){
            val response = homeDataSource.questionResponse()
            if (response.error != null){
                onResult(false, response.error!!.message!!, null)
            }else{
                onResult(true, response.message!!, response)
            }
        }
    }
    }
