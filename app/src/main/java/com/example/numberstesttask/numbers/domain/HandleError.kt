package com.example.numberstesttask.numbers.domain

import com.example.numberstesttask.R
import com.example.numberstesttask.numbers.presentaion.ManageResources

interface HandleError<T> {

    fun handle(e: Exception): T

    class Base(
        private val manageResources: ManageResources
    ) : HandleError<String> {

        override fun handle(e: Exception) = manageResources.string(
            when (e) {
                is DomainException.NoInternetConnection -> R.string.no_connection_message
                else -> R.string.service_is_unavailable
            }
        )
    }
}