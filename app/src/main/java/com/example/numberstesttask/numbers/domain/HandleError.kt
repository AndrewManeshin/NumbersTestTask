package com.example.numberstesttask.numbers.domain

import com.example.numberstesttask.R
import com.example.numberstesttask.numbers.presentaion.ManageResources

interface HandleError {

    fun handle(e: Exception): String

    class Base(
        private val manageResources: ManageResources
    ) : HandleError {

        override fun handle(e: Exception) = manageResources.string(
                when (e) {
                    is DomainException.NoConnectionException -> R.string.no_connection_message
                    else -> R.string.service_is_unavailable
                }
            )
    }
}