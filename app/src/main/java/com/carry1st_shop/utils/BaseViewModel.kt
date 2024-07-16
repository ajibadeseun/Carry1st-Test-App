package com.carry1st_shop.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.cert.CertPathValidatorException

open class BaseViewModel: ViewModel() {

    private var mJob: Job? = null

    protected fun <T> handleBaseRequest(stateFlow: MutableStateFlow<T>, errorHandler: CoroutinesErrorHandler, request: () -> Flow<T>) {
        mJob = viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                Timber.tag("coroutine").d(error)
                errorHandler.onError(error.localizedMessage ?: "Error occurred! Please try again.", error)
            }
        }){
            request().catch { cause ->
                when(cause){
                    is SocketTimeoutException -> {
                        errorHandler.onError("A network timeout has occurred!", cause)
                    }

                    is UnknownHostException ->{
                        errorHandler.onError("Oops! It seems like you're offline. Please check your internet connection and try again.", cause)
                    }

                    is CertPathValidatorException -> {
                        errorHandler.onError("A network error has occurred!", cause)
                    }

                    is ConnectException -> {
                        errorHandler.onError("A network error has occurred!", cause)
                    }

                    else -> {
                        Timber.tag("Exception").d(cause)
                        errorHandler.onError( "Error occurred! Please try again.", cause)
                    }
                }
            }.collect {
                withContext(Dispatchers.Main) {
                    stateFlow.value = it
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }
}

interface CoroutinesErrorHandler {
    fun onError(message:String, throwable: Throwable? = null)
}
