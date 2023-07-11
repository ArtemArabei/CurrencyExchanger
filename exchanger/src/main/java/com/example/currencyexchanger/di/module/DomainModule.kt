package com.example.currencyexchanger.di.module

import com.example.currencyexchanger.domain.repository.CurrencyExchangerRepository
import com.example.currencyexchanger.domain.usecase.GetCurrencyExchangeRatesUseCase
import com.example.currencyexchanger.domain.usecase.GetCurrencyExchangeRatesUseCaseImpl
import com.example.currencyexchanger.utils.DispatcherProvider
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideGetCurrencyExchangeDataUseCase(
        repository: CurrencyExchangerRepository,
        dispatcherProvider: DispatcherProvider
    ): GetCurrencyExchangeRatesUseCase {
        return GetCurrencyExchangeRatesUseCaseImpl(repository, dispatcherProvider)
    }

}