package com.example.numberstesttask.numbers.data.cache

import com.example.numberstesttask.numbers.data.NumberData
import com.example.numberstesttask.numbers.data.cloud.FetchNumber

interface NumbersCacheDataSource : FetchNumber {

    suspend fun contains(number: String): Boolean

    suspend fun saveNumber(numberData: NumberData)

    suspend fun allNumbers(): List<NumberData>
}
