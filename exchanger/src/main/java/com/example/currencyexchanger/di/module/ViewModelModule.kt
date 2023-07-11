package com.example.currencyexchanger.di.module

import androidx.lifecycle.ViewModel
import com.example.currencyexchanger.di.ViewModelKey
import com.example.currencyexchanger.presentation.view.ExchangerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ExchangerViewModel::class)
    abstract fun bindExchangerViewModel(viewModel: ExchangerViewModel): ViewModel

}