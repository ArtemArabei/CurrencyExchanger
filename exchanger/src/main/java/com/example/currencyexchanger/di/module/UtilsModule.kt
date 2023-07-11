package com.example.currencyexchanger.di.module

import com.example.currencyexchanger.utils.DispatcherProvider
import dagger.Module
import dagger.Provides

/*  This dependency should be in the CORE module, and be provided by App.Component
But in this case it isn't necessary to create a new module just for it*/

@Module
class UtilsModule {

    @Provides
    @ExchangerScope
    fun dispatcher(): DispatcherProvider = DispatcherProvider.CoroutineDispatcherProvider()

}