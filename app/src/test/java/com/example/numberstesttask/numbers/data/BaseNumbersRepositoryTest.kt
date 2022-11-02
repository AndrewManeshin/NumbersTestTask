package com.example.numberstesttask.numbers.data

import com.example.numberstesttask.numbers.data.cache.NumbersCacheDataSource
import com.example.numberstesttask.numbers.data.cloud.NumbersCloudDataSource
import com.example.numberstesttask.numbers.domain.DomainException
import com.example.numberstesttask.numbers.domain.NumberFact
import com.example.numberstesttask.numbers.domain.NumbersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

internal class BaseNumbersRepositoryTest {

    private lateinit var repository: NumbersRepository
    private lateinit var cloudDataSource: TestNumbersCloudDataSource
    private lateinit var cacheDataSource: TestNumbersCacheDataSource

    @Before
    fun setUp() {
        cloudDataSource = TestNumbersCloudDataSource()
        cacheDataSource = TestNumbersCacheDataSource()
        val mapper = NumberDataToDomain()
        repository = BaseNumbersRepository(
            cacheDataSource,
            cloudDataSource,
            HandleDataRequest.Base(HandleDomainError(), cacheDataSource, mapper),
            mapper
        )
    }

    @Test
    fun `test all numbers`() = runBlocking {
        cacheDataSource.replaceData(
            listOf(
                NumberData("4", "fact of 4"),
                NumberData("5", "fact of 5")
            )
        )

        val actual = repository.allNumbers()
        val expected = listOf(
            NumberFact("4", "fact of 4"),
            NumberFact("5", "fact of 5")
        )

        actual.forEachIndexed { index, item ->
            assertEquals(expected[index], item)
        }
        assertEquals(1, cacheDataSource.allNumbersCalledCount)
    }

    @Test
    fun `test number fact not cached success`() = runBlocking {
        cloudDataSource.makeExpected(NumberData("6", "fact of 6"))
        cacheDataSource.replaceData(emptyList())

        val actual = repository.numberFact("6")
        val expected = NumberFact("6", "fact of 6")

        assertEquals(expected, actual)
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cacheDataSource.numberFactCalledList.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(1, cloudDataSource.numberFactCalledCount)
        assertEquals(NumberData("6", "fact of 6"), cacheDataSource.data[0])
    }

    @Test(expected = DomainException.NoInternetConnection::class)
    fun `test number fact not cached failure`() = runBlocking {
        cloudDataSource.changeConnection(false)
        cacheDataSource.replaceData(emptyList())

        repository.numberFact("7")
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cacheDataSource.numberFactCalledList.size)
        assertEquals(0, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(1, cloudDataSource.numberFactCalledCount)
    }

    @Test
    fun `test number fact cached`() = runBlocking {
        cloudDataSource.changeConnection(true)
        cloudDataSource.makeExpected(NumberData("4", "fact of 4 cloud"))
        cacheDataSource.replaceData(listOf(NumberData("4", "fact of 4")))

        val actual = repository.numberFact("4")
        val expected = NumberFact("4", "fact of 4")

        assertEquals(expected, actual)
        assertEquals(true, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cloudDataSource.numberFactCalledCount)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(1, cacheDataSource.numberFactCalledList.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals("4", cacheDataSource.numberFactCalledList[0])
    }

    @Test
    fun `test random number fact not cached success`() = runBlocking {
        cloudDataSource.makeExpected(NumberData("6", "fact of 6"))
        cacheDataSource.replaceData(emptyList())

        val actual = repository.randomNumberFact()
        val expected = NumberFact("6", "fact of 6")

        assertEquals(expected, actual)
        assertEquals(0, cloudDataSource.numberFactCalledCount)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalledList.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(NumberData("6", "fact of 6"), cacheDataSource.data[0])
    }

    @Test(expected = DomainException.NoInternetConnection::class)
    fun `test random number fact not cached failure`() = runBlocking {
        cloudDataSource.changeConnection(false)
        cacheDataSource.replaceData(emptyList())

        repository.numberFact("7")

        assertEquals(0, cacheDataSource.numberFactCalledList.size)
        assertEquals(0, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(0, cloudDataSource.numberFactCalledCount)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)
    }

    @Test
    fun `test random number fact cached`() = runBlocking {
        cloudDataSource.changeConnection(true)
        cloudDataSource.makeExpected(NumberData("4", "fact of 4 cloud"))
        cacheDataSource.replaceData(listOf(NumberData("4", "fact of 4")))

        val actual = repository.randomNumberFact()
        val expected = NumberFact("4", "fact of 4 cloud")

        assertEquals(expected, actual)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalledList.size)
    }


    private class TestNumbersCloudDataSource : NumbersCloudDataSource {

        var numberFactCalledCount = 0
        var randomNumberFactCalledCount = 0
        private var thereIsConnection = true
        private var numberData = NumberData("", "")

        fun changeConnection(connected: Boolean) {
            thereIsConnection = connected
        }

        fun makeExpected(numberData: NumberData) {
            this.numberData = numberData
        }

        override suspend fun number(number: String): NumberData {
            numberFactCalledCount++
            return if (thereIsConnection)
                numberData
            else
                throw UnknownHostException()
        }

        override suspend fun randomNumber(): NumberData {
            randomNumberFactCalledCount++
            return if (thereIsConnection)
                numberData
            else
                throw UnknownHostException()
        }
    }

    private class TestNumbersCacheDataSource : NumbersCacheDataSource {

        val containsCalledList = mutableListOf<Boolean>()
        var saveNumberFactCalledCount = 0
        var numberFactCalledList = mutableListOf<String>()
        var allNumbersCalledCount = 0
        val data = mutableListOf<NumberData>()

        fun replaceData(newData: List<NumberData>) {
            data.clear()
            data.addAll(newData)
        }

        override suspend fun contains(number: String): Boolean {
            val result = data.find { it.map(NumberData.Mapper.Matches(number)) } != null
            containsCalledList.add(result)
            return result
        }

        override suspend fun saveNumber(numberData: NumberData) {
            saveNumberFactCalledCount++
            data.add(numberData)
        }

        override suspend fun allNumbers(): List<NumberData> {
            allNumbersCalledCount++
            return data
        }

        override suspend fun number(number: String): NumberData {
            numberFactCalledList.add(number)
            return data[0]
        }
    }
}