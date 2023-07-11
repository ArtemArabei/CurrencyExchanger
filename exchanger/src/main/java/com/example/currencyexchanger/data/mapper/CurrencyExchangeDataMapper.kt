package com.example.currencyexchanger.data.mapper

import com.example.currencyexchanger.data.model.CurrencyExchangeDataDTO
import com.example.currencyexchanger.domain.model.CurrencyExchangeData
import com.example.currencyexchanger.domain.model.CurrencyRate

fun CurrencyExchangeDataDTO.toDomain(): CurrencyExchangeData {
    return CurrencyExchangeData(
        base,
        date,
        rates.map { CurrencyRate(it.key, it.value) }
    )
}