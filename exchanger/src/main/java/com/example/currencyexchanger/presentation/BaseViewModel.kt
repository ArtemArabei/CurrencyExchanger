package com.example.currencyexchanger.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchanger.utils.DispatcherProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel(
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val error = MutableLiveData<String>()
    val errorToShow: LiveData<String> = error

    protected fun <T> runCoroutine(
        dispatcher: CoroutineDispatcher = this.dispatcherProvider.main(),
        block: suspend CoroutineScope.() -> T
    ) = viewModelScope.launch(dispatcher) {
        try {
            block()
        } catch (t: Throwable) {
            if (t !is CancellationException) {
                error.postValue(t.message ?: ERROR_MESSAGE)
            } else throw t
        }
    }

    companion object {
        private const val ERROR_MESSAGE = "Something went wrong!"
    }

}