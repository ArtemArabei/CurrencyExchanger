package com.example.currencyexchanger.presentation.model

import com.example.currencyexchanger.domain.model.CurrencyExchangeData

sealed class RequestResult {
    class Success(val currencyExchangeData: CurrencyExchangeData): RequestResult()
    object Loading : RequestResult()
}