package com.example.currencyexchanger.presentation.view

import com.example.currencyexchanger.domain.model.CurrencyBalance
import com.example.currencyexchanger.domain.model.CurrencyExchangeData
import com.example.currencyexchanger.domain.model.ExchangeMessage
import com.example.currencyexchanger.domain.usecase.GetCurrencyExchangeRatesUseCase
import com.example.currencyexchanger.presentation.BaseViewModel
import com.example.currencyexchanger.presentation.model.RequestResult
import com.example.currencyexchanger.utils.DispatcherProvider
import com.example.currencyexchanger.utils.roundToNDecimalPlacesToString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ExchangerViewModel @Inject constructor(
    private val getCurrencyExchangeRatesUseCase: GetCurrencyExchangeRatesUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    companion object {
        private const val BASE_BALANCE = 1000.00
        private const val BASE_CURRENCY = "EUR"
        private const val ZERO_BALANCE = 0.00
        private const val COMMISSION_FEE = 0.007
        private const val NUMBER_OF_FREE_EXCHANGES = 5
        private const val TIME_TO_UPDATE = 5000L
        private const val START_COUNTER_VALUE = 1
    }

    private val requestResult = MutableStateFlow<RequestResult>(RequestResult.Loading)
    val requestResultToShow get() = requestResult.asStateFlow()

    private val currencyBalances = MutableStateFlow<List<CurrencyBalance>>(emptyList())
    val currencyBalancesToShow get() = currencyBalances.asStateFlow()

    private val currenciesToSell = MutableStateFlow<List<String>>(emptyList())
    val currenciesToSellToShow get() = currenciesToSell.asStateFlow()

    private val currenciesToReceive = MutableStateFlow<List<String>>(emptyList())
    val currenciesToReceiveToShow get() = currenciesToReceive.asStateFlow()

    private val exchangeMessage = MutableStateFlow<ExchangeMessage>(ExchangeMessage.NoMessage)
    val exchangeResultToShow get() = exchangeMessage.asStateFlow()

    private val currencyRates = mutableMapOf<String, Double>()

    private var areStartBalancesCreated = false

    private var baseCurrency = BASE_CURRENCY

    private var counter: Int = START_COUNTER_VALUE

    private var isCommissionFee = false

    init {
        receiveCurrencyExchangeData()
    }

    private fun receiveCurrencyExchangeData() {
        runCoroutine {
            while (true) {
                val currencyExchangeData = getCurrencyExchangeRatesUseCase.invoke()
                requestResult.value = RequestResult.Success(currencyExchangeData)
                delay(TIME_TO_UPDATE)
            }
        }
    }

    fun receiveCurrencyExchangeRates(currencyExchangeData: CurrencyExchangeData) {
        runCoroutine {
            currencyExchangeData.rates.forEach {
                currencyRates[it.name] = it.rate
            }
            baseCurrency = currencyExchangeData.base
        }
    }

    fun createStartBalances(currencyExchangeData: CurrencyExchangeData) {
        if (!areStartBalancesCreated) {
            runCoroutine {
                val startCurrencyBalances = mutableListOf<CurrencyBalance>()
                currencyExchangeData.rates.forEach { rate ->
                    if (rate.name == baseCurrency) {
                        startCurrencyBalances.add(CurrencyBalance(rate.name, BASE_BALANCE))

                    } else {
                        startCurrencyBalances.add(CurrencyBalance(rate.name, ZERO_BALANCE))
                    }
                }
                currencyBalances.emit(startCurrencyBalances)
                areStartBalancesCreated = true
            }
        }
    }

    fun updateListCurrenciesToSell() {
        runCoroutine {
            val currenciesToSellBuf = mutableListOf<String>()
            currencyBalances.value.forEach {
                if (it.balance > ZERO_BALANCE) {
                    currenciesToSellBuf.add(it.name)
                }
            }
            currenciesToSell.emit(emptyList())
            currenciesToSell.emit(currenciesToSellBuf)
        }
    }

    fun updateListCurrenciesToReceive() {
        runCoroutine {
            currenciesToReceive.emit(currencyRates.keys.toList())
        }
    }

    fun showConversionInfo(
        amountOfCurrencyToSell: String, currencyToSell: String, currencyToReceive: String
    ): Double {

        val balanceOfCurrencyToSell = currencyBalances.value.first { it.name == currencyToSell }
        val commission = getAmountOfCommission(amountOfCurrencyToSell.toDouble())
        val amountOfCurrencyToReceive =
            if (balanceOfCurrencyToSell.balance >= amountOfCurrencyToSell.toDouble()) {
                convertCurrencies(
                    currencyToSell,
                    currencyToReceive,
                    (amountOfCurrencyToSell.toDouble() - commission)
                )

            } else {
                ZERO_BALANCE
            }
        return amountOfCurrencyToReceive
    }

    private fun convertCurrencies(
        startCurrency: String, finalCurrency: String, sumToConvert: Double
    ): Double {
        val rateOfCurrencyToSell = currencyRates.getValue(startCurrency)
        val rateOfCurrencyToReceive = currencyRates.getValue(finalCurrency)
        val sumToReceive = if (startCurrency == baseCurrency) {
            sumToConvert * rateOfCurrencyToReceive
        } else {
            sumToConvert / rateOfCurrencyToSell * rateOfCurrencyToReceive
        }
        return sumToReceive
    }

    fun makeExchange(
        amountOfCurrencyToSell: Double,
        amountOfCurrencyToReceive: Double,
        currencyToSell: String,
        currencyToReceive: String
    ) {
        runCoroutine {
            var commission = ""
            var isSuccess = false
            val currentCurrencyBalances = currencyBalances.value
            currentCurrencyBalances.forEach {
                if (it.name == currencyToSell &&
                    it.balance >= amountOfCurrencyToSell
                    && amountOfCurrencyToSell > 0.00
                ) {
                    it.balance -= amountOfCurrencyToSell
                    commission =
                        String.format("%.2f", getAmountOfCommission(amountOfCurrencyToSell))
                    isSuccess = true
                }
            }
            currentCurrencyBalances.forEach {
                if (it.name == currencyToReceive
                    && isSuccess
                ) {
                    it.balance += amountOfCurrencyToReceive
                }
            }
            checkIsCommissionFeeForNextExchange()
            currencyBalances.value = emptyList()
            currencyBalances.value = currentCurrencyBalances
            if (isSuccess) {
                counter++
                exchangeMessage.emit(
                    ExchangeMessage.SuccessMessage(
                        amountOfCurrencyToSell.roundToNDecimalPlacesToString(2),
                        currencyToSell,
                        amountOfCurrencyToReceive.roundToNDecimalPlacesToString(2),
                        currencyToReceive,
                        commission
                    )
                )
                exchangeMessage.emit(
                    ExchangeMessage.NoMessage
                )
            }
        }
    }

    private fun checkIsCommissionFeeForNextExchange() {
        if (counter >= NUMBER_OF_FREE_EXCHANGES) {
            isCommissionFee = true
        }
    }

    private fun getAmountOfCommission(amountOfCurrencyToSell: Double) = if (isCommissionFee) {
        amountOfCurrencyToSell * COMMISSION_FEE
    } else {
        ZERO_BALANCE
    }
}