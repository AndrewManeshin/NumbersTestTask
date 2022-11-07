package com.example.numberstesttask.main.sl

import com.example.numberstesttask.main.presentaion.MainViewModel

class MainModule(
    private val provideNavigation: ProvideNavigation
) : Module<MainViewModel> {

    override fun viewModel() = MainViewModel(provideNavigation.provideNavigation())
}