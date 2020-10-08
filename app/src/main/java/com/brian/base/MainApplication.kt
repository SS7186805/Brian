package com.brian.base

import android.app.Application
import com.ayuka.mvvmdemo.data.network.error.NetworkErrorHandler
import com.ayuka.mvvmdemo.data.network.error.NetworkErrorHandlerImpl
import com.brian.network.APIService
import com.brian.network.dataSource.authDataSource.AuthenticationDataSource
import com.brian.network.dataSource.authDataSource.AuthenticationDataSourceImp
import com.brian.network.dataSource.homeFragmentDataSource.HomeDataSource
import com.brian.network.dataSource.homeFragmentDataSource.HomeDataSourceImp
import com.brian.network.dataSource.myProfileDataSource.MyProfileDataSource
import com.brian.network.dataSource.myProfileDataSource.MyProfileDataSourceImp
import com.brian.providers.resources.ResourcesProvider
import com.brian.providers.resources.ResourcesProviderImpl
import com.brian.repository.authRepository.authRepository.AuthenticationRepository
import com.brian.repository.authRepository.authRepository.AuthenticationRepositoryImpl
import com.brian.repository.authRepository.homeRepository.HomeRepository
import com.brian.repository.authRepository.homeRepository.HomeRepositoryImp
import com.brian.repository.authRepository.myProfileRepository.MyProfileRepository
import com.brian.repository.authRepository.myProfileRepository.MyProfileRepositoryImp
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.brian.viewModels.login.LoginViewModelFactory
import com.brian.viewModels.myProfile.MyProfileViewModelFactory
import com.brian.viewModels.register.RegisterViewModelFactory
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
        bind<HomeDataSource>() with singleton{
            HomeDataSourceImp(instance())
        }
        bind<MyProfileDataSource>() with singleton {
            MyProfileDataSourceImp(instance())
        }
        bind<MyProfileRepository>() with singleton {
            MyProfileRepositoryImp(instance())
        }
        //Factory
        bind() from singleton { RegisterViewModelFactory(instance(),instance()) }
        bind() from singleton { HomescreenViewModelFactory(instance(),instance()) }
        bind() from singleton { MyProfileViewModelFactory(instance()) }
        bind() from singleton { LoginViewModelFactory(instance()) }
    }

    companion object {
        private lateinit var instance: MainApplication
        fun get(): MainApplication = instance
    }


}