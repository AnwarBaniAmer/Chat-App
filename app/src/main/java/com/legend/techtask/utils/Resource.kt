package com.legend.techtask.utils


sealed class ViewState<out ResultType> {
    data class Success<ResultType>(val data: ResultType?) : ViewState<ResultType>()
    data class Error(val message: String) : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
    object Idle : ViewState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$message]"
            Loading -> "Loading"
            else -> ""
        }
    }

}
