package com.example.numberstesttask.numbers.domain

abstract class DomainException : IllegalStateException() {

    class NoConnectionException : DomainException()

    class ServiceUnavailableException : DomainException()
}