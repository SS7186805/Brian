package com.brian.base

import android.app.Application
import com.brian.viewModels.register.RegisterViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MainApplication : Application(),KodeinAware{

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MainApplication))



        //Factory
        bind() from singleton { RegisterViewModelFactory() }


    }

}