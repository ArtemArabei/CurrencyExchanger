package com.example.currencyexchanger.domain.model

data class CurrencyExchangeData(
    val base: String,
    val date: String,
    val rates: List<CurrencyRate>
)