package com.example.currencyexchanger

import android.app.Application
import com.example.currencyexchanger.di.AppComponent
import com.example.currencyexchanger.di.DaggerAppComponent
import com.example.currencyexchanger.di.ExchangerComponent
import com.example.currencyexchanger.di.ExchangerComponentProvider

class App : Application(), ExchangerComponentProvider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger()
    }

    private fun initDagger(): AppComponent {
        return DaggerAppComponent.builder()
            .setContext(context = this)
            .build()
    }

    override fun provideExchangerComponent(): ExchangerComponent {
        return appComponent.exchangerComponent()
            .create()
    }

}

