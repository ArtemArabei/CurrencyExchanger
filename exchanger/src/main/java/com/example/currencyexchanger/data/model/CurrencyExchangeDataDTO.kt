package com.example.currencyexchanger.data.model

data class CurrencyExchangeDataDTO(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)