package com.example.phonecontactssearch.presentation

import com.example.phonecontactssearch.domain.di.useCaseModule
import com.example.phonecontactssearch.domain.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(listOf(useCaseModule, viewModelModule))
        }
    }
}