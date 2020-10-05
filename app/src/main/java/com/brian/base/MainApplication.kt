package com.brian.base

import android.app.Application
import com.ayuka.mvvmdemo.data.network.error.NetworkErrorHandler
import com.ayuka.mvvmdemo.data.network.error.NetworkErrorHandlerImpl
import com.brian.network.APIService
import com.brian.network.dataSource.AuthenticationDataSource
import com.brian.network.dataSource.AuthenticationDataSourceImp
import com.brian.providers.resources.ResourcesProvider
import com.brian.providers.resources.ResourcesProviderImpl
import com.brian.repository.authRepository.AuthenticationRepository
import com.brian.repository.authRepository.AuthenticationRepositoryImpl
import com.brian.viewModels.login.LoginViewModelFactory
import com.brian.viewModels.register.RegisterViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bindings.Singleton
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MainApplication : Application(),KodeinAware{


    override fun onCreate() {
        super.onCreate()
        instance = this

    }

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MainApplication))

        bind() from singleton { APIService() }
        bind<AuthenticationRepository>() with singleton{
            AuthenticationRepositoryImpl(instance())
        }
        bind<ResourcesProvider>() with singleton {
            ResourcesProviderImpl(instance())
        }
        bind<NetworkErrorHandler>() with singleton {
            NetworkErrorHandlerImpl(instance())
        }
        bind<AuthenticationDataSource>() with singleton {
            AuthenticationDataSourceImp(instance())
        }



        //Factory
        bind() from singleton { RegisterViewModelFactory(instance(),instance()) }
        bind() from singleton { LoginViewModelFactory(instance()) }
    }

    companion object {
        private lateinit var instance: MainApplication
        fun get(): MainApplication = instance
    }


}