package com.brian.network

import android.content.Intent
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.brian.base.MainApplication
import com.brian.base.Prefs
import com.brian.models.*
import com.brian.views.activities.AccountHandlerActivity
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


const val BASE_URL = "http://1.6.98.142/brian_m4/"

interface APIService {


    @Multipart
    @POST("api/v1/register")
    suspend fun signUp(
        @PartMap request: HashMap<String, RequestBody>,
        @Part profile_picture: MultipartBody.Part?
    ): BaseResponse

    @POST("api/v1/login")
    suspend fun login(@Body request: RegisterRequest): BaseResponse

    @POST("api/v1/forgot-password")
    suspend fun forgot(@Body request: RegisterRequest): BaseResponse

    @GET("api/v1/get-defensive-situation")
    suspend fun getDefensiveSituation(): DefensiveResponse

    @GET("api/v1/my-friends")
    suspend fun getMyFriends(): ResponseMyFriends

    @GET("api/v1/challenge-list-admin")
    suspend fun getChallenges(): ResponseChallengeType

    @POST("api/v1/change-password")
    suspend fun changePassword(@Body changeRequest: ChangePassword): BaseResponse

    @POST("api/v1/create-challenge")
    suspend fun createChallenge(@Body createChallenge: CreateChallengeParams): ResponseCreateChallenge

    @POST("api/v1/search-friends")
    suspend fun searchUsers(@Body params: SearchQuery): ResponseSearchUsers


    @POST("api/v1/users")
    suspend fun searchMyUsers(@Body params: SearchQuery): ResponseMyFriends



    @POST("api/v1/send-friend-request")
    suspend fun sendFriendRequest(@Body params: SendRequestParams): ResponseSendRequest

    @POST("api/v1/cancel-request")
    suspend fun cancelFriendRequest(@Body params: SendRequestParams): ResponseSendRequest

    @POST("api/v1/accept-reject-friend-req")
    suspend fun acceptRejectRequest(@Body params: SendRequestParams): ResponseSendRequest

    @POST("api/v1/logout")
    suspend fun logOut(): BaseResponse


    @Multipart
    @POST("api/v1/profile")
    suspend fun editProfile(
        @PartMap request: HashMap<String, RequestBody>,
        @Part profile_picture: MultipartBody.Part?
    ): BaseResponse

    @GET("api/v1/get-question-randomly")
    suspend fun questionResponse(): QuestionResponse

    @GET("api/v1/profile")
    suspend fun viewProfile(): BaseResponse


    @GET("api/v1/training-videos")
    suspend fun getTrainingVideos(@Query("page") page: Int?): ResponseTrainingVideos

    @GET("api/v1/buzz-feeds")
    suspend fun getBuzzFeed(@Query("page") page: Int?): ResponseBuzzFeed


    companion object {

        private fun getAPIService(): APIService {
            val client =
                OkHttpClient.Builder().addInterceptor(provideHeaderInterceptor()!!)
                    .addInterceptor(ForbiddenInterceptor())
                    .addInterceptor(provideHttpLoggingInterceptor()!!)
                    .addInterceptor(ExceptionInterceptor())
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES).build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(APIService::class.java)
        }


        fun getErrorMessageFromGenericResponse(
            exception: Exception
        ): String? {
            var error = ""
            try {
                when (exception) {
                    is HttpException -> {
                        val body = exception.response()?.errorBody()
                        val adapter = Gson().getAdapter(BaseResponse::class.java)
                        val errorParser = adapter.fromJson(body?.string())
                        error = errorParser.message ?: exception.message()
                    }
                    is ConnectException -> {
                        error = "Connection_Error"
                    }
                    is SocketTimeoutException -> {
                        error = "TimeoutError"
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                return error
            }
        }


        private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d("API Logging", "response => $message")
                    }
                })
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
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
                            .addHeader("Authorization", "Bearer $accessToken").build()
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
                } else if (response.code == 403) {
                    Prefs.init().isLogIn = "false"
                    android.os.Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            MainApplication.get().applicationContext,
                            "Unauthenciated",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    MainApplication.get().applicationContext.startActivity(
                        Intent(
                            MainApplication.get().applicationContext,
                            AccountHandlerActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    return response
                }

                return response
            }

        }

        operator fun invoke() = getAPIService()
    }
}