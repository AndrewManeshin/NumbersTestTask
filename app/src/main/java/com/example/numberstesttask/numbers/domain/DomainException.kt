package com.example.numberstesttask.numbers.domain

abstract class DomainException : IllegalStateException() {

    class NoInternetConnection : DomainException()

    class ServiceUnavailable : DomainException()
}