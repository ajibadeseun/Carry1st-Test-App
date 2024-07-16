package com.carry1st_shop.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import retrofit2.Response
import kotlinx.coroutines.flow.flowOn

sealed class Result<out T> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val errorResponse: ErrorResponse?) : Result<Nothing>()
}

data class ErrorResponse(
    val errors: List<String>?,
    val isSuccessful: Boolean?,
    val message: String?,
    val title: String?,
)

fun <T> apiRequestFlow(call: suspend () -> Response<T>): Flow<Result<T>> = channelFlow {

    val response = call()
    try {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                send(Result.Success(data))
            }
        } else {
            when (response.code()) {
                500, 501, 502, 503, 504, 509, 511 -> {
                    send(
                        Result.Error(
                            ErrorResponse(emptyList(),false,"An error has occurred","Oops!")
                        )
                    )
                }

                else -> {
                    response.errorBody()?.let { error ->
                        val er = error.string()
                        error.close()

                        val parsedError: ErrorResponse =
                            Gson().fromJson(er, ErrorResponse::class.java)
                        send(Result.Error(parsedError))

                    }
                }
            }

        }
    } catch (e: Exception) {
        send(Result.Error(errorResponse = null))
    } ?: send(Result.Error(ErrorResponse(emptyList(),false,"An error has occurred","Oops!")))
}.flowOn(Dispatchers.IO)


@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}