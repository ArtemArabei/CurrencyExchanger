package com.example.currencyexchanger.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {

    fun io(): CoroutineDispatcher

    fun main(): CoroutineDispatcher

    fun computation(): CoroutineDispatcher

    class CoroutineDispatcherProvider : DispatcherProvider {
        override fun io(): CoroutineDispatcher = Dispatchers.IO
        override fun main(): CoroutineDispatcher = Dispatchers.Main
        override fun computation(): CoroutineDispatcher = Dispatchers.Default
    }

}