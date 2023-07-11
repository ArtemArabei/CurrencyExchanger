package com.example.currencyexchanger.domain.model

sealed class ExchangeMessage {
    class SuccessMessage(
        val amountOfCurrencyToSell: String,
        val currencyToSell: String,
        val amountOfCurrencyToReceive: String,
        val currencyToReceive: String,
        val commission: String
    ): ExchangeMessage()

    object NoMessage: ExchangeMessage()
}

