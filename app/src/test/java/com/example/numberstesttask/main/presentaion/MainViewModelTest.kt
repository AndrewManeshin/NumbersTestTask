package com.example.numberstesttask.main.presentaion

import com.example.numberstesttask.numbers.presentaion.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest : BaseTest() {

    @Test
    fun `test navigation at start`() {
        val navigation = TestNavigationCommunication()
        val viewModel = MainViewModel(navigation)

        viewModel.init(true)
        assertEquals(1, navigation.count)
        assertEquals(NavigationStrategy.Replace(Screen.Numbers), navigation.strategy)

        viewModel.init(false)
        assertEquals(1, navigation.count)
    }
}