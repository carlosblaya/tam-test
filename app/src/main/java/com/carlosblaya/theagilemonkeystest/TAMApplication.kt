package com.carlosblaya.theagilemonkeystest

import android.app.Application
import android.content.Context
import com.carlosblaya.theagilemonkeystest.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TAMApplication: Application(){

    companion object {
        lateinit var instance: TAMApplication
        lateinit var context: Context
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        //Koin
        startKoin {
            // declare used Android context
            androidContext(this@TAMApplication)
            // declare modules
            modules(retrofitModule, repositoryModule, serviceModule, dbModule,viewModelModule,mappersModule)
        }
    }

}