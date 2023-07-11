package com.example.currencyexchanger.di.module

import com.example.currencyexchanger.data.api.CurrencyExchangerApi
import com.example.currencyexchanger.data.repository.CurrencyExchangerRepositoryImpl
import com.example.currencyexchanger.domain.repository.CurrencyExchangerRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
class DataModule {

    @Provides
    @ExchangerScope
    fun provideCurrencyExchangerApi(): CurrencyExchangerApi {
        val currencyExchangerApi by lazy {
            Retrofit.Builder()
                .baseUrl(CURRENCY_EXCHANGER_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create<CurrencyExchangerApi>()
        }
        return currencyExchangerApi
    }

    @Provides
    @ExchangerScope
    fun provideCurrencyExchangerRepository(currencyExchangerApi: CurrencyExchangerApi):
            CurrencyExchangerRepository {
        return CurrencyExchangerRepositoryImpl(currencyExchangerApi)
    }

    companion object {
        private const val CURRENCY_EXCHANGER_API_URL = "https://developers.paysera.com/tasks/api/"
    }

}