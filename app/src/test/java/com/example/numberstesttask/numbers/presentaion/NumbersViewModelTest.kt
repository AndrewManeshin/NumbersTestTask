package com.example.numberstesttask.numbers.presentaion

import com.example.numberstesttask.numbers.domain.NumberFact
import com.example.numberstesttask.numbers.domain.NumberUiMapper
import com.example.numberstesttask.numbers.domain.NumbersInteractor
import com.example.numberstesttask.numbers.domain.NumbersResult
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NumbersViewModelTest : BaseTest() {

    private lateinit var viewModel: NumbersViewModel
    private lateinit var interactor: TestNumbersInteractor
    private lateinit var manageResources: TestManageResources
    private lateinit var communications: TestNumbersCommunications

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun init() {
        Dispatchers.setMain(mainThreadSurrogate)

        communications = TestNumbersCommunications()
        interactor = TestNumbersInteractor()
        manageResources = TestManageResources()
        //1. initialize
        viewModel = NumbersViewModel(
            TestDispatchersList(),
            manageResources,
            communications,
            interactor,
            NumbersResultMapper(communications, NumberUiMapper()),
        )
    }

    /**
     * Initial test
     * At start fetch data and show it
     * then try to get some data successfully
     * then re-init ans check the result
     */
    @Test
    fun `test init and re-init`() = runBlocking {

        interactor.changeExpectedResult(NumbersResult.Success())
        //2. action
        viewModel.init(isFirsRun = true)
        //3. check
        assertEquals(true, communications.progressCalledList[0])
        assertEquals(2, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[1])

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(true, communications.stateCalledList[0] is UiState.Success)

        assertEquals(0, communications.numbersList.size)
        assertEquals(0, communications.timesShowList)

        //get some data
        interactor.changeExpectedResult(NumbersResult.Failure("no internet connection"))
        viewModel.fetchRandomNumberFact()

        assertEquals(true, communications.progressCalledList[2])

        assertEquals(1, interactor.fetchAboutRandomNumberCalledList.size)

        assertEquals(4, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[3])

        assertEquals(2, communications.stateCalledList.size)
        assertEquals(UiState.Error("no internet connection"), communications.stateCalledList[1])
        assertEquals(0, communications.timesShowList)

        viewModel.init(isFirsRun = false)
        assertEquals(4, communications.progressCalledList.size)
        assertEquals(2, communications.stateCalledList.size)
        assertEquals(0, communications.timesShowList)

    }

    /**
     * Try to get information about empty number
     */
    @Test
    fun `fact about empty number`() = runBlocking {

        manageResources.makeExpectedAnswer("entered number is empty")
        viewModel.fetchNumberFact("")

        assertEquals(0, interactor.fetchAboutNumberCalledList.size)

        assertEquals(0, communications.progressCalledList.size)

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(UiState.Error("entered number is empty"), communications.stateCalledList[0])

        assertEquals(0, communications.timesShowList)
    }

    /**
     * Try to get information about some number
     */
    @Test
    fun `fact about some number`() = runBlocking {

        interactor.changeExpectedResult(
            NumbersResult.Success(
                listOf(
                    NumberFact(
                        "45",
                        "fact about 45"
                    )
                )
            )
        )
        viewModel.fetchNumberFact("45")

        assertEquals(true, communications.progressCalledList[0])

        assertEquals(1, interactor.fetchAboutNumberCalledList.size)
        assertEquals(NumbersResult.Success(listOf(NumberFact("45", "fact about 45"))), interactor.fetchAboutNumberCalledList[0])

        assertEquals(2, communications.progressCalledList.size)
        assertEquals(false, communications.progressCalledList[1])

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(true, communications.stateCalledList[0] is UiState.Success)

        assertEquals(1, communications.timesShowList)
        assertEquals(NumberUi("45", "fact about 45"), communications.numbersList[0])
    }

    private class TestManageResources : ManageResources {

        private var string = ""

        fun makeExpectedAnswer(expected: String) {
            string = expected
        }

        override fun string(id: Int) = string
    }

    private class TestNumbersInteractor : NumbersInteractor {

        private var result: NumbersResult = NumbersResult.Success(emptyList())

        val initCalledList = mutableListOf<NumbersResult>()
        val fetchAboutNumberCalledList = mutableListOf<NumbersResult>()
        val fetchAboutRandomNumberCalledList = mutableListOf<NumbersResult>()

        fun changeExpectedResult(newResult: NumbersResult) {
            result = newResult
        }

        override suspend fun init(): NumbersResult {
            initCalledList.add(result)
            return result
        }

        override suspend fun factAboutNumber(number: String): NumbersResult {
            fetchAboutNumberCalledList.add(result)
            return result
        }

        override suspend fun factAboutRandomNumber(): NumbersResult {
            fetchAboutRandomNumberCalledList.add(result)
            return result
        }
    }

    private class TestDispatchersList : DispatchersList {
        override fun io() = TestCoroutineDispatcher()
        override fun ui() = TestCoroutineDispatcher()
    }
}