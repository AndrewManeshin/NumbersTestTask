package com.example.numberstesttask.numbers.presentaion

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numberstesttask.R
import com.example.numberstesttask.details.presentation.NumberDetailsFragment
import com.example.numberstesttask.main.presentaion.Init
import com.example.numberstesttask.main.presentaion.NavigationCommunication
import com.example.numberstesttask.main.presentaion.NavigationStrategy
import com.example.numberstesttask.main.presentaion.Screen
import com.example.numberstesttask.numbers.domain.NumbersInteractor

interface NumbersViewModel : Init, ObserveNumbers, FetchNumbers, ClearError {

    fun showDetails(item: NumberUi)

    class Base(
        private val handleResult: HandleNumbersRequest,
        private val manageResources: ManageResources,
        private val communications: NumbersCommunications,
        private val interactor: NumbersInteractor,
        private val navigationCommunication: NavigationCommunication.Mutate,
        private val detailsMapper: NumberUi.Mapper<String>,
    ) : ViewModel(), NumbersViewModel {


        override fun observeProgress(owner: LifecycleOwner, observer: Observer<Int>) {
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
                communications.showState(UiState.ShowError(manageResources.string(R.string.empty_number_error_message)))
            else
                handleResult.handle(viewModelScope) {
                    interactor.factAboutNumber(number)
                }
        }

        override fun clearError() = communications.showState(UiState.ClearError())

        override fun showDetails(item: NumberUi) {
            interactor.saveDetails(item.map(detailsMapper))
            navigationCommunication.map(
                NavigationStrategy.Add(Screen.Details)
            )
        }
    }
}

interface FetchNumbers {

    fun fetchRandomNumberFact()

    fun fetchNumberFact(number: String)
}

interface ClearError {
    fun clearError()
}