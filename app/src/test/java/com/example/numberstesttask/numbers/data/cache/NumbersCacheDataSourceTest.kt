package com.example.numberstesttask.numbers.data.cache

import com.example.numberstesttask.numbers.data.NumberData
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class NumbersCacheDataSourceTest {

    @Test
    fun `test all numbers empty`() = runBlocking {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao, TestMapper(5))

        val actual = dataSource.allNumbers()
        val expected = emptyList<NumberCache>()
        assertEquals(expected, actual)
    }

    @Test
    fun `all numbers not empty`() = runBlocking {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao, TestMapper(5))

        dao.data.add(NumberCache("1", "fact 1", 1))
        dao.data.add(NumberCache("2", "fact 2", 2))

        val actualList = dataSource.allNumbers()
        val expectedList = listOf(
            NumberData("1", "fact 1"),
            NumberData("2", "fact 2")
        )

        actualList.forEachIndexed { index, actual ->
            assertEquals(expectedList[index], actual)
        }
    }

    @Test
    fun `test contains`() = runBlocking {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao, TestMapper(5))

        dao.data.add(NumberCache("3", "fact 3", 3))

        val actual = dataSource.contains("3")
        val expected = true

        assertEquals(expected, actual)
    }

    @Test
    fun `test not contains`() = runBlocking {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao, TestMapper(5))

        dao.data.add(NumberCache("3", "fact 3", 3))

        val actual = dataSource.contains("5")
        val expected = false

        assertEquals(expected, actual)
    }

    @Test
    fun `test save`() = runBlocking {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao, TestMapper(5))

        dataSource.saveNumber(NumberData("4", "fact 4"))

        assertEquals(NumberCache("4", "fact 4", 5), dao.data[0])
    }

    @Test
    fun `test number contains`() = runBlocking {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao, TestMapper(8))

        dao.data.add(NumberCache("10", "fact 10", 10))

        val actual = dataSource.number("10")
        val expected = NumberData("10", "fact 10")

        assertEquals(expected, actual)
    }

    @Test
    fun `test number not exist`() = runBlocking {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao, TestMapper(8))

        dao.data.add(NumberCache("10", "fact 10", 10))

        val actual = dataSource.number("20")
        val expected = NumberData("", "")

        assertEquals(expected, actual)
    }
}

private class TestDao : NumbersDao {

    val data = mutableListOf<NumberCache>()

    override fun allNumbers(): List<NumberCache> =
        data

    override fun insert(number: NumberCache) {
        data.add(number)
    }

    override fun number(number: String): NumberCache? =
        data.find { it.number == number }
}

private class TestMapper(private val date: Long) : NumberData.Mapper<NumberCache> {
    override fun map(id: String, fact: String) = NumberCache(id, fact, date)
}