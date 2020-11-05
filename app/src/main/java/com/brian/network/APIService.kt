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

    @POST("api/v1/submit-answers")
    suspend fun submitAnswers(@Body request: SubmitAnswerParams): BaseResponse

    @POST("api/v1/forgot-password")
    suspend fun forgot(@Body request: RegisterRequest): BaseResponse

    @GET("api/v1/get-defensive-situation")
    suspend fun getDefensiveSituation(): DefensiveResponse


    @POST("api/v1/select-defensive-situation")
    suspend fun getSelectDefensiveSituation(@Body sendRequestParams: SubmitAnswerParams): BaseResponse


    @GET("api/v1/my-friends")
    suspend fun getMyFriends(@Query("page") page: Int): ResponseMyFriends

    @GET("api/v1/challenge-list-admin")
    suspend fun getChallenges(@Query("page") page: Int): ResponseChallengeType

    @GET("api/v1/my-challenges")
    suspend fun getMyChallenges(@Query("page") int: Int): ResponseMyChallenges

    @GET("api/v1/challenge-req")
    suspend fun getChallengesRequests(@Query("page") page: Int): ResponseMyChallenges

    @GET("api/v1/all-chats")
    suspend fun getAllChats(@Query("page") page: Int): ResponseAllChats

    @POST("api/v1/get-chat-by-room-id")
    suspend fun getAllMessages(@Query("page") page: Int,@Body getAllMessagesParams: GetAllMessagesParams): ResponseGetAllMessages

    @POST("api/v1/create_chat_room")
    suspend fun createChatRoom(@Body createChatRoomParams: CreateChatRoomParams): ResponseCreateChatRoom

    @POST("api/v1/remove-chat")
    suspend fun removeChat(@Body createChatRoomParams: GetAllMessagesParams): BaseResponse

    @POST("api/v1/change-password")
    suspend fun changePassword(@Body changeRequest: ChangePassword): BaseResponse

    @POST("api/v1/create-challenge")
    suspend fun createChallenge(@Body createChallenge: CreateChallengeParams): ResponseCreateChallenge

    @Multipart
    @POST("api/v1/accept-challenge")
    suspend fun acceptChallengeRequest(
        @PartMap request: HashMap<String, RequestBody>,
        @Part profile_picture: MultipartBody.Part?
    ): BaseResponse


    @POST("api/v1/approved-rejected-challenge")
    suspend fun approveRejectMyChallenge(
        @Body createChallenge: AcceptChallengeParams
    ): BaseResponse

    @POST("api/v1/cancel-challenge-request")
    suspend fun cancelMyChallenge(@Body createChallenge: CreateChatRoomParams): BaseResponse

    @POST("api/v1/search-friends")
    suspend fun searchUsers(@Query("page") page: Int, @Body params: SearchQuery): ResponseSearchUsers


    @POST("api/v1/users")
    suspend fun searchMyUsers(@Query("page") page: Int, @Body params: SearchQuery): ResponseMyFriends


    @POST("api/v1/send-friend-request")
    suspend fun sendFriendRequest(@Body params: SendRequestParams): ResponseSendRequest

    @POST("api/v1/cancel-request")
    suspend fun cancelFriendRequest(@Body params: SendRequestParams): ResponseSendRequest

    @POST("api/v1/accept-reject-friend-req")
    suspend fun acceptRejectRequest(@Body params: SendRequestParams): ResponseSendRequest

    @POST("api/v1/remove-friend")
    suspend fun removeFriend(@Body params: SendRequestParams): BaseResponse

    @POST("api/v1/contact-us")
    suspend fun contactUs(@Body params: ContactUsParams): BaseResponse

    @Multipart
    @POST("api/v1/create-team")
    suspend fun createTeam(
        @PartMap request: HashMap<String, RequestBody>,
        @Part profile_picture: MultipartBody.Part?
    ): ResponseCreateTeam

    @Multipart
    @POST("api/v1/send-message")
    suspend fun sendMessage(
        @PartMap request: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part?
    ): ResponseSendMessage

    @POST("api/v1/logout")
    suspend fun logOut(): BaseResponse


    @Multipart
    @POST("api/v1/profile")
    suspend fun editProfile(
        @PartMap request: HashMap<String, RequestBody>,
        @Part profile_picture: MultipartBody.Part?
    ): BaseResponse

    @GET("api/v1/get-question-randomly/{id}")
    suspend fun questionResponse(@Path("id") id: Int): QuestionResponse

    @GET("api/v1/leaderboard/challenges")
    suspend fun getChallengesLeaderBoard(): ResponseLeaderboard

    @GET("api/v1/leaderboard/players")
    suspend fun getPlayers(): ResponseLeaderboard

    @GET("api/v1/my-status")
    suspend fun getStats(): ResponseMyStats

    @GET("api/v1/teams")
    suspend fun getMyTeams(@Query("page") page: Int): ResponseMyTeams


    @GET("api/v1/profile/{id}")
    suspend fun viewProfile(@Path("id") id: String): BaseResponse


    @GET("api/v1/training-videos")
    suspend fun getTrainingVideos(@Query("page") page: Int?): ResponseTrainingVideos

    @POST("api/v1/training-videos-by-category-id")
    suspend fun getTrainingVideosWithCategory(@Query("page") page: Int, @Body params: QueryParams): ResponseTrainingVideosWithCategory


    @GET("api/v1/get-training-video-data-management")
    suspend fun getDataTraining(): ResponseDataManagement

    @GET("api/v1/get-defensive-situation-data-management")
    suspend fun getDataHome(): ResponseDataManagement

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