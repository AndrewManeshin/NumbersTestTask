package com.example.numberstesttask.numbers.data

import com.example.numberstesttask.numbers.domain.DomainException
import com.example.numberstesttask.numbers.domain.HandleError
import java.net.UnknownHostException

class HandleDomainError : HandleError<Exception> {

    override fun handle(e: Exception) = when (e) {
        is UnknownHostException -> DomainException.NoInternetConnection()
        else -> DomainException.ServiceUnavailable()
    }
}