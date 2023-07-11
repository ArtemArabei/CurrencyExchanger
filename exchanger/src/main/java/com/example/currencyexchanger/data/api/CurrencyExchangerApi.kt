package com.example.currencyexchanger.data.api

import com.example.currencyexchanger.data.model.CurrencyExchangeDataDTO
import retrofit2.http.GET

interface CurrencyExchangerApi {

    @GET(CURRENCY_EXCHANGE_RATES_API_REQUEST)
    suspend fun getCurrencyExchangeData(): CurrencyExchangeDataDTO

    companion object {
        private const val CURRENCY_EXCHANGE_RATES_API_REQUEST = "currency-exchange-rates"
    }

}