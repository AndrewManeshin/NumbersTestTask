package com.example.numberstesttask.numbers.data

interface NumbersCacheDataSource : FetchNumber {

    suspend fun contains(number: String): Boolean

    suspend fun saveNumber(numberData: NumberData)

    suspend fun allNumbers(): List<NumberData>
}
