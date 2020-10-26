package com.brian.base

import android.app.Application
import com.ayuka.mvvmdemo.data.network.error.NetworkErrorHandler
import com.ayuka.mvvmdemo.data.network.error.NetworkErrorHandlerImpl
import com.brian.network.APIService
import com.brian.dataSource.authDataSource.AuthenticationDataSource
import com.brian.dataSource.authDataSource.AuthenticationDataSourceImp
import com.brian.dataSource.homeFragmentDataSource.HomeDataSource
import com.brian.dataSource.homeFragmentDataSource.HomeDataSourceImp
import com.brian.dataSource.myProfileDataSource.MyProfileDataSource
import com.brian.dataSource.myProfileDataSource.MyProfileDataSourceImp
import com.brian.dataSource.trainingDataSource.TrainingAndBuzzDataSource
import com.brian.dataSource.trainingDataSource.TrainingAndBuzzDataSourceImp
import com.brian.dataSource.users.UsersDataSource
import com.brian.dataSource.users.UsersDataSourceImp
import com.brian.providers.resources.ResourcesProvider
import com.brian.providers.resources.ResourcesProviderImpl
import com.brian.repository.authRepository.AuthenticationRepository
import com.brian.repository.authRepository.AuthenticationRepositoryImpl
import com.brian.repository.homeRepository.HomeRepository
import com.brian.repository.homeRepository.HomeRepositoryImp
import com.brian.repository.myProfileRepository.MyProfileRepository
import com.brian.repository.myProfileRepository.MyProfileRepositoryImp
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepository
import com.brian.repository.trainingVideosRepositary.TrainingAndBuzzRepositoryImp
import com.brian.repository.usersRepositary.UsersRepository
import com.brian.repository.usersRepositary.UsersRepositoryImp
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.brian.viewModels.login.LoginViewModelFactory
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

        bind<TrainingAndBuzzRepository>() with singleton{
            TrainingAndBuzzRepositoryImp(instance())
        }
        bind<HomeDataSource>() with singleton{
            HomeDataSourceImp(instance())
        }
        bind<MyProfileDataSource>() with singleton {
            MyProfileDataSourceImp(instance())
        }

        bind<TrainingAndBuzzDataSource>() with singleton {
            TrainingAndBuzzDataSourceImp(instance())
        }
        bind<UsersDataSource>() with singleton {
            UsersDataSourceImp(instance())
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
    }

    companion object {
        private lateinit var instance: MainApplication
        fun get(): MainApplication = instance
    }


}