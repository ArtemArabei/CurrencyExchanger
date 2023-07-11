package com.example.currencyexchanger.data.repository

import com.example.currencyexchanger.data.api.CurrencyExchangerApi
import com.example.currencyexchanger.data.model.CurrencyExchangeDataDTO
import com.example.currencyexchanger.domain.repository.CurrencyExchangerRepository

class CurrencyExchangerRepositoryImpl(
    private val currencyExchangerApi: CurrencyExchangerApi,
) : CurrencyExchangerRepository {

    override suspend fun getCurrencyExchangeData(): CurrencyExchangeDataDTO =
        currencyExchangerApi.getCurrencyExchangeData()

}