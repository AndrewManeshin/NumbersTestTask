package com.example.numberstesttask

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.numberstesttask.numbers.data.cache.NumberCache
import com.example.numberstesttask.numbers.data.cache.NumbersDao
import com.example.numberstesttask.numbers.data.cache.NumbersDatabase
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomTest {

    private lateinit var db : NumbersDatabase
    private lateinit var dao: NumbersDao

    @Before
    fun setUp() {
        val context : Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, NumbersDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.numbersDao()
    }

    @After
    @Throws(IOException::class)
    fun clear() {
        db.close()
    }

    @Test
    fun test_add_and_check()  {
        val number = NumberCache("12", "fact12", 10)

        assertEquals(null, dao.number("12"))

        dao.insert(number)
        val actualList = dao.allNumbers()
        assertEquals(number, actualList[0])

        val actual = dao.number("12")
        assertEquals(number, actual)
    }

    @Test
    fun test_add_2_times() {
        val number = NumberCache("12", "fact12", 10)
        dao.insert(number)
        var actualList = dao.allNumbers()
        assertEquals(number, actualList[0])

        val new = NumberCache("12", "fact12", 100)
        dao.insert(new)
        actualList = dao.allNumbers()
        assertEquals(1, actualList.size)
        assertEquals(new, actualList[0])
    }
}