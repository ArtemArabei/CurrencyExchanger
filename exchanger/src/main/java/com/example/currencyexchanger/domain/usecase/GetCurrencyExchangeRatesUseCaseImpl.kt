package com.example.currencyexchanger.domain.usecase

import com.example.currencyexchanger.data.mapper.toDomain
import com.example.currencyexchanger.domain.model.CurrencyExchangeData
import com.example.currencyexchanger.domain.repository.CurrencyExchangerRepository
import com.example.currencyexchanger.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class GetCurrencyExchangeRatesUseCaseImpl(
    private val repository: CurrencyExchangerRepository,
    private val dispatcherProvider: DispatcherProvider
) : GetCurrencyExchangeRatesUseCase {

    override suspend operator fun invoke(): CurrencyExchangeData =
        withContext(dispatcherProvider.io()) { repository.getCurrencyExchangeData().toDomain() }

}