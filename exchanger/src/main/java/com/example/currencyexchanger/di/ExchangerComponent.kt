package com.example.currencyexchanger.di

import com.example.currencyexchanger.di.module.DataModule
import com.example.currencyexchanger.di.module.DomainModule
import com.example.currencyexchanger.di.module.ExchangerScope
import com.example.currencyexchanger.di.module.UtilsModule
import com.example.currencyexchanger.di.module.ViewModelFactoryModule
import com.example.currencyexchanger.di.module.ViewModelModule
import com.example.currencyexchanger.presentation.view.ExchangerFragment
import dagger.Subcomponent

@ExchangerScope
@Subcomponent(
    modules = [
        DomainModule::class,
        DataModule::class,
        ViewModelModule::class,
        ViewModelFactoryModule::class,
        UtilsModule::class
    ]
)
interface ExchangerComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ExchangerComponent
    }

    fun inject(exchangerFragment: ExchangerFragment)
}