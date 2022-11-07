package com.example.numberstesttask.numbers.presentaion

import android.view.View
import com.example.numberstesttask.main.presentaion.NavigationStrategy
import com.example.numberstesttask.main.presentaion.Screen
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

    private lateinit var navigation: TestNavigationCommunication
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

        navigation = TestNavigationCommunication()
        communications = TestNumbersCommunications()
        interactor = TestNumbersInteractor()
        manageResources = TestManageResources()
        val handleNumbersRequest = HandleNumbersRequest.Base(
            TestDispatchersList(),
            communications,
            NumbersResultMapper(communications, NumberUiMapper())
        )
        val detailsMapper = TestUiMapper()
        viewModel = NumbersViewModel.Base(
            handleNumbersRequest,
            manageResources,
            communications,
            interactor,
            navigation,
            detailsMapper
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
        assertEquals(View.VISIBLE, communications.progressCalledList[0])
        assertEquals(2, communications.progressCalledList.size)
        assertEquals(View.GONE, communications.progressCalledList[1])

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(true, communications.stateCalledList[0] is UiState.Success)

        assertEquals(0, communications.numbersList.size)
        assertEquals(0, communications.timesShowList)

        //get some data
        interactor.changeExpectedResult(NumbersResult.Failure("no internet connection"))
        viewModel.fetchRandomNumberFact()

        assertEquals(View.VISIBLE, communications.progressCalledList[2])

        assertEquals(1, interactor.fetchAboutRandomNumberCalledList.size)

        assertEquals(4, communications.progressCalledList.size)
        assertEquals(View.GONE, communications.progressCalledList[3])

        assertEquals(2, communications.stateCalledList.size)
        assertEquals(UiState.ShowError("no internet connection"), communications.stateCalledList[1])
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
        assertEquals(
            UiState.ShowError("entered number is empty"),
            communications.stateCalledList[0]
        )

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

        assertEquals(View.VISIBLE, communications.progressCalledList[0])

        assertEquals(1, interactor.fetchAboutNumberCalledList.size)
        assertEquals(
            NumbersResult.Success(listOf(NumberFact("45", "fact about 45"))),
            interactor.fetchAboutNumberCalledList[0]
        )

        assertEquals(2, communications.progressCalledList.size)
        assertEquals(View.GONE, communications.progressCalledList[1])

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(true, communications.stateCalledList[0] is UiState.Success)

        assertEquals(1, communications.timesShowList)
        assertEquals(NumberUi("45", "fact about 45"), communications.numbersList[0])
    }

    @Test
    fun `test clear error`() {
        viewModel.clearError()

        assertEquals(1, communications.stateCalledList.size)
        assertEquals(true, communications.stateCalledList[0] is UiState.ClearError)
    }

    @Test
    fun `test navigation details`() {
        viewModel.showDetails(NumberUi("12", "fact about 12"))

        assertEquals("12 fact about 12", interactor.details)
        assertEquals(1, navigation.count)
        assertEquals(NavigationStrategy.Add(Screen.Details), navigation.strategy)
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
        var details = ""

        val initCalledList = mutableListOf<NumbersResult>()
        val fetchAboutNumberCalledList = mutableListOf<NumbersResult>()
        val fetchAboutRandomNumberCalledList = mutableListOf<NumbersResult>()

        override fun saveDetails(details: String) {
            this.details = details
        }

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

    private class TestUiMapper : NumberUi.Mapper<String> {
        override fun map(id: String, fact: String) = "$id $fact"
    }
}