package com.example.numberstesttask.numbers.data

interface NumbersCloudDataSource : FetchNumber {
    suspend fun randomNumberFact(): NumberData
}

interface FetchNumber {
    suspend fun number(number: String): NumberData
}
