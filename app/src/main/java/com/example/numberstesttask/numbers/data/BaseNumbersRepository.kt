package com.example.numberstesttask.numbers.data

import com.example.numberstesttask.numbers.domain.NumberFact
import com.example.numberstesttask.numbers.domain.NumbersRepository

class BaseNumbersRepository(
    private val cacheDataSource: NumbersCacheDataSource,
    private val cloudDataSource: NumbersCloudDataSource,
    private val handleDataRequest: HandleDataRequest,
    private val mapperToDomain: NumberDataToDomain
) : NumbersRepository {

    override suspend fun allNumbers(): List<NumberFact> {
        val data = cacheDataSource.allNumbers()
        return data.map { numberData ->
            numberData.map(mapperToDomain)
        }
    }

    override suspend fun numberFact(number: String) = handleDataRequest.handle {
        val dataSource = if (cacheDataSource.contains(number))
            cacheDataSource
        else
            cloudDataSource
        dataSource.number(number)
    }

    override suspend fun randomNumberFact() = handleDataRequest.handle {
        cloudDataSource.randomNumber()
    }
}