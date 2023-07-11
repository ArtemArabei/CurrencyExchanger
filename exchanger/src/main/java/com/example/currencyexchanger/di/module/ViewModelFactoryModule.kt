package com.example.currencyexchanger.di.module

import androidx.lifecycle.ViewModelProvider
import com.example.currencyexchanger.di.AppViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelFactoryModule {

    @Binds
    fun factory(impl: AppViewModelFactory): ViewModelProvider.Factory

}