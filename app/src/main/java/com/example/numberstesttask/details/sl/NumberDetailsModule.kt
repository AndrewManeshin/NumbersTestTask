package com.example.numberstesttask.details.sl

import com.example.numberstesttask.details.presentation.NumberDetailsViewModel
import com.example.numberstesttask.main.sl.Module
import com.example.numberstesttask.main.sl.ProvideNumberDetails

class NumberDetailsModule(
    private val provideNumberDetails: ProvideNumberDetails
) : Module<NumberDetailsViewModel> {

    override fun viewModel() = NumberDetailsViewModel(provideNumberDetails.provideNumberDetails())
}