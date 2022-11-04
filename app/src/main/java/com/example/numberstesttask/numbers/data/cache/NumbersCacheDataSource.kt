package com.example.numberstesttask.numbers.data.cache

import com.example.numberstesttask.numbers.data.NumberData
import com.example.numberstesttask.numbers.data.cloud.FetchNumber
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface NumbersCacheDataSource : FetchNumber {

    suspend fun contains(number: String): Boolean

    suspend fun saveNumber(numberData: NumberData)

    suspend fun allNumbers(): List<NumberData>

    class Base(
        private val dao: NumbersDao,
        private val dataToCache: NumberData.Mapper<NumberCache>
    ) : NumbersCacheDataSource {

        private val mutex = Mutex()

        override suspend fun contains(number: String) = mutex.withLock {
            dao.number(number) != null
        }

        override suspend fun saveNumber(numberData: NumberData) = mutex.withLock {
            dao.insert(numberData.map(dataToCache))
        }

        override suspend fun allNumbers(): List<NumberData> = mutex.withLock {
            dao.allNumbers().map { NumberData(it.number, it.fact) }
        }

        override suspend fun number(number: String): NumberData = mutex.withLock {
            val numberCache = dao.number(number) ?: NumberCache("", "", 0)
            return NumberData(numberCache.number, numberCache.fact)
        }
    }
}