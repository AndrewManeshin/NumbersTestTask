package com.example.numberstesttask.numbers.presentaion

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numberstesttask.R
import com.example.numberstesttask.numbers.domain.NumbersInteractor
import com.example.numberstesttask.numbers.domain.NumbersResult
import kotlinx.coroutines.launch

class NumbersViewModel(
    private val handleResult: HandleNumbersRequest,
    private val manageResources: ManageResources,
    private val communications: NumbersCommunications,
    private val interactor: NumbersInteractor,
) : ViewModel(), ObserveNumbers, FetchNumbers {


    override fun observeProgress(owner: LifecycleOwner, observer: Observer<Boolean>) {
        communications.observeProgress(owner, observer)
    }

    override fun observeState(owner: LifecycleOwner, observer: Observer<UiState>) {
        communications.observeState(owner, observer)
    }

    override fun observeList(owner: LifecycleOwner, observer: Observer<List<NumberUi>>) {
        communications.observeList(owner, observer)
    }

    override fun init(isFirsRun: Boolean) {
        if (isFirsRun) {
            handleResult.handle(viewModelScope) {
                interactor.init()
            }
        }
    }

    override fun fetchRandomNumberFact() {
        handleResult.handle(viewModelScope) {
            interactor.factAboutRandomNumber()
        }
    }

    override fun fetchNumberFact(number: String) {
        if (number.isEmpty())
            communications.showState(UiState.Error(manageResources.string(R.string.empty_number_error_message)))
        else
            handleResult.handle(viewModelScope) {
                interactor.factAboutNumber(number)
            }
    }
}

interface FetchNumbers {

    fun init(isFirsRun: Boolean)

    fun fetchRandomNumberFact()

    fun fetchNumberFact(number: String)
}