package com.brian.base

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.ayuka.mvvmdemo.data.network.error.NetworkErrorHandler
import com.ayuka.mvvmdemo.data.network.error.NetworkErrorHandlerImpl
import com.brian.dataSource.authDataSource.AuthenticationDataSource
import com.brian.dataSource.authDataSource.AuthenticationDataSourceImp
import com.brian.dataSource.challenges.ChallenegesDataSourceImp
import com.brian.dataSource.challenges.ChallengesDataSource
import com.brian.dataSource.homeFragmentDataSource.HomeDataSource
import com.brian.dataSource.homeFragmentDataSource.HomeDataSourceImp
import com.brian.dataSource.messages.MessagesDataSource
import com.brian.dataSource.messages.MessagesDataSourceImp
import com.brian.dataSource.myProfileDataSource.MyProfileDataSource
import com.brian.dataSource.myProfileDataSource.MyProfileDataSourceImp
import com.brian.dataSource.trainingDataSource.TrainingAndBuzzDataSource
import com.brian.dataSource.trainingDataSource.TrainingAndBuzzDataSourceImp
import com.brian.dataSource.users.UsersDataSource
import com.brian.dataSource.users.UsersDataSourceImp
import com.brian.network.APIService
import com.brian.providers.resources.ResourcesProvider
import com.brian.providers.resources.ResourcesProviderImpl
import com.brian.repository.authRepository.AuthenticationRepository
import com.brian.repository.authRepository.AuthenticationRepositoryImpl
import com.brian.repository.challenges.ChallengesRepository
import com.brian.repository.challenges.ChallengesRepositoryImp
import com.brian.repository.homeRepository.HomeRepository
import com.brian.repository.homeRepository.HomeRepositoryImp
import com.brian.repository.messages.MessagesRepository
import com.brian.repository.messages.MessagesRepositoryImp
import com.brian.repository.myProfileRepository.MyProfileRepository
import com.brian.repository.myProfileRepository.MyProfileRepositoryImp
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepository
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepositoryImp
import com.brian.repository.usersRepositary.UsersRepository
import com.brian.repository.usersRepositary.UsersRepositoryImp
import com.brian.viewModels.challenges.ChallengesViewModelFactory
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.brian.viewModels.login.LoginViewModelFactory
import com.brian.viewModels.messages.MessagesViewModelFactory
import com.brian.viewModels.myProfile.MyProfileViewModelFactory
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.viewModels.trainingVideos.BuzzFeedViewModelFactory
import com.brian.viewModels.trainingVideos.TrainingsViewModelFactory
import com.brian.viewModels.users.UsersViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


class MainApplication : Application(),KodeinAware{


    override fun onCreate() {
        super.onCreate()
        instance = this

    }

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MainApplication))

        bind() from singleton { APIService() }

        bind<ResourcesProvider>() with singleton {
            ResourcesProviderImpl(instance())
        }
        bind<NetworkErrorHandler>() with singleton {
            NetworkErrorHandlerImpl(instance())
        }
        bind<AuthenticationDataSource>() with singleton {
            AuthenticationDataSourceImp(instance())
        }
        bind<AuthenticationRepository>() with singleton{
            AuthenticationRepositoryImpl(instance())
        }
        bind<HomeRepository>() with singleton{
            HomeRepositoryImp(instance())
        }

        bind<MessagesRepository>() with singleton{
            MessagesRepositoryImp(instance())
        }

        bind<ChallengesRepository>() with singleton{
            ChallengesRepositoryImp(instance())
        }

        bind<TrainingAndBuzzRepository>() with singleton{
            TrainingAndBuzzRepositoryImp(instance())
        }
        bind<HomeDataSource>() with singleton{
            HomeDataSourceImp(instance())
        }
        bind<MyProfileDataSource>() with singleton {
            MyProfileDataSourceImp(instance())
        }
        bind<MessagesDataSource>() with singleton {
            MessagesDataSourceImp(instance())
        }

        bind<TrainingAndBuzzDataSource>() with singleton {
            TrainingAndBuzzDataSourceImp(instance())
        }
        bind<UsersDataSource>() with singleton {
            UsersDataSourceImp(instance())
        }

        bind<ChallengesDataSource>() with singleton {
            ChallenegesDataSourceImp(instance())
        }
        bind<MyProfileRepository>() with singleton {
            MyProfileRepositoryImp(instance())
        }

        bind<UsersRepository>() with singleton {
            UsersRepositoryImp(instance())
        }
        //Factory
        bind() from singleton { RegisterViewModelFactory(instance(),instance()) }
        bind() from singleton { HomescreenViewModelFactory(instance(),instance()) }
        bind() from singleton { MyProfileViewModelFactory(instance()) }
        bind() from singleton { LoginViewModelFactory(instance()) }
        bind() from singleton { BuzzFeedViewModelFactory(instance(), instance()) }
        bind() from singleton { TrainingsViewModelFactory(instance(), instance()) }
        bind() from singleton { UsersViewModelFactory(instance(), instance()) }
        bind() from singleton { ChallengesViewModelFactory(instance(), instance(), instance()) }
        bind() from singleton { MessagesViewModelFactory(instance(), instance()) }
    }

    companion object {
        lateinit var instance: MainApplication
        fun get(): MainApplication = instance
        fun hasNetwork(): Boolean {
            return instance.checkIfHasNetwork()
        }
    }


    private fun checkIfHasNetwork(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


}