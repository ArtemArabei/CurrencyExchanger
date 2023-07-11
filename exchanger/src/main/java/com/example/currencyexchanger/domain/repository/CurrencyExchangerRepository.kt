package com.example.currencyexchanger.domain.repository

import com.example.currencyexchanger.data.model.CurrencyExchangeDataDTO

interface CurrencyExchangerRepository {
    suspend fun getCurrencyExchangeData(): CurrencyExchangeDataDTO
}