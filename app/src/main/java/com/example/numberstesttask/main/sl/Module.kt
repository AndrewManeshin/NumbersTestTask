package com.example.numberstesttask.main.sl

import androidx.lifecycle.ViewModel

interface Module<T: ViewModel> {

    fun viewModel(): T
}