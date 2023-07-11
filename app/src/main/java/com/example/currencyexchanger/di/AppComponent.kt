package com.example.currencyexchanger.di

import android.content.Context
import com.example.currencyexchanger.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun setContext(context: Context): Builder

        fun build(): AppComponent

    }

    fun exchangerComponent(): ExchangerComponent.Factory

    fun inject(mainActivity: MainActivity)

}