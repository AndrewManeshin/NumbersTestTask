package com.example.numberstesttask.numbers.presentaion

import com.example.numberstesttask.numbers.domain.NumberFact
import com.example.numberstesttask.numbers.domain.NumberUiMapper
import org.junit.Assert.*
import org.junit.Test

class NumbersResultMapperTest : BaseTest() {

    @Test
    fun `test error`() {
        val communications = TestNumbersCommunications()
        val mapper = NumbersResultMapper(communications, NumberUiMapper())

        mapper.map(emptyList(), "not empty message")

        assertEquals(UiState.ShowError("not empty message"), communications.stateCalledList[0])
    }

    @Test
    fun `test success with empty list`() {
        val communications = TestNumbersCommunications()
        val mapper = NumbersResultMapper(communications, NumberUiMapper())

        mapper.map(emptyList(), "")

        assertEquals(true, communications.stateCalledList[0] is UiState.Success)
        assertEquals(0, communications.timesShowList)
    }

    @Test
    fun `test success with not empty list`() {
        val communications = TestNumbersCommunications()
        val mapper = NumbersResultMapper(communications, NumberUiMapper())

        mapper.map(listOf(NumberFact("0", "fact")), "")

        assertEquals(true, communications.stateCalledList[0] is UiState.Success)
        assertEquals(1, communications.timesShowList)
        assertEquals(NumberUi("0", "fact"), communications.numbersList[0])
    }
}