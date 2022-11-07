package com.example.numberstesttask.main.presentaion

import com.example.numberstesttask.details.presentation.NumberDetailsFragment
import com.example.numberstesttask.numbers.presentaion.NumbersFragment

sealed class Screen {

    abstract fun fragment(): Class<out BaseFragment<*>>

    object Details : Screen() {
        override fun fragment(): Class<out BaseFragment<*>> = NumberDetailsFragment::class.java
    }

    object Numbers : Screen() {
        override fun fragment(): Class<out BaseFragment<*>> = NumbersFragment::class.java
    }
}