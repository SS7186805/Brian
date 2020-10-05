package com.brian.network

import android.text.TextUtils
import android.util.Log
import com.brian.BuildConfig
import com.brian.base.Prefs
import com.brian.models.BaseResponse
import com.brian.models.RegisterRequest
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.IOException


const val BASE_URL = "http://1.6.98.142/brain/"

interface APIService {

    @POST("api/v1/register")
    suspend fun signUp(@Body request: RegisterRequest): BaseResponse

    @POST("api/v1/login")
    suspend fun login(@Body request: RegisterRequest):BaseResponse

    @POST("api/v1/forgot-password")
    suspend fun forgot(@Body request: RegisterRequest):BaseResponse






    companion object {

        private fun getAPIService(): APIService {
            val client =
                OkHttpClient.Builder().addInterceptor(provideHeaderInterceptor()!!)
                    .addInterceptor(ForbiddenInterceptor())
                    .addInterceptor(provideHttpLoggingInterceptor()!!)
                    .addInterceptor(ExceptionInterceptor()).build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(APIService::class.java)
        }


        private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d("API Logging", "response => $message")
                    }
                })
            httpLoggingInterceptor.setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
            return httpLoggingInterceptor
        }

        private fun provideHeaderInterceptor(): Interceptor? {
            return object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    //*Code for request with auth token
                    val accessToken: String = Prefs.init().accessToken
                    return if (!TextUtils.isEmpty(accessToken)) {
                        val request: Request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("x-access-token", "Bearer $accessToken").build()
                        chain.proceed(request)
                    } else {
                        val request: Request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json").build()
                        chain.proceed(request)
                    }
                }
            }
        }


        class ForbiddenInterceptor : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request()
                return chain.proceed(request)
            }
        }

        class ExceptionInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val response = chain.proceed(request)

                if (response.code == 500) {
                    return response
                } else if (response.code == 402) {
                    return response
                }

                return response
            }

        }
        operator fun invoke() = getAPIService()
    }
}