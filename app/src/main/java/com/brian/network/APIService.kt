package com.brian.network

import android.content.Intent
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.brian.BuildConfig
import com.brian.base.MainApplication
import com.brian.base.Prefs
import com.brian.models.*
import com.brian.views.activities.AccountHandlerActivity
import com.brian.views.activities.HomeActivity
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.android.parcel.RawValue
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
import java.util.logging.Handler


const val BASE_URL = "http://1.6.98.142/brian_m4/"

interface APIService {

//    @Multipart
//    @POST("api/v1/register")
//    suspend fun signUp(@Part name: MultipartBody.Part,
//    @Part email : MultipartBody.Part,
//    @Part dob:MultipartBody.Part,
//    @Part user_type:MultipartBody.Part,
//    @Part password:MultipartBody.Part,
//    @Part device_type:MultipartBody.Part,
//    @Part device_token:MultipartBody.Part,
//    @Part profile_picture: MultipartBody.Part?): BaseResponse

    //    @Multipart
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

    @POST("api/v1/change-password")
    suspend fun changePassword(@Body changeRequest: ChangePassword): BaseResponse

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