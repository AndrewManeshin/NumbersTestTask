package com.example.numberstesttask.main.sl

import androidx.lifecycle.ViewModel
import com.example.numberstesttask.details.presentation.NumberDetailsViewModel
import com.example.numberstesttask.details.sl.NumberDetailsModule
import com.example.numberstesttask.main.presentaion.MainViewModel
import com.example.numberstesttask.numbers.presentaion.NumbersViewModel
import com.example.numberstesttask.numbers.sl.NumbersModule

interface DependencyContainer {

    fun <T : ViewModel> module(clazz: Class<T>): Module<*>

    class Error : DependencyContainer {
        override fun <T : ViewModel> module(clazz: Class<T>): Module<*> {
            throw IllegalStateException("no module found for $clazz")
        }
    }

    class Base(
        private val core: Core,
        private val dependencyContainer: DependencyContainer = Error()
    ) : DependencyContainer {
        override fun <T : ViewModel> module(clazz: Class<T>): Module<*> = when (clazz) {
            MainViewModel::class.java -> MainModule(core)
            NumbersViewModel.Base::class.java -> NumbersModule(core)
            NumberDetailsViewModel::class.java -> NumberDetailsModule(core)
            else -> dependencyContainer.module(clazz)
        }
    }
}