package com.example.currencyexchanger.domain.usecase

import com.example.currencyexchanger.domain.model.CurrencyExchangeData

interface GetCurrencyExchangeRatesUseCase {
    suspend operator fun invoke(): CurrencyExchangeData
}