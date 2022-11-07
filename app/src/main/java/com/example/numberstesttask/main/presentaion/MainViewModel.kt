package com.example.numberstesttask.main.presentaion

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.numberstesttask.numbers.presentaion.Communication
import com.example.numberstesttask.numbers.presentaion.NumbersFragment

class MainViewModel(
    private val navigationCommunication: NavigationCommunication.Mutable
) : ViewModel(), Init, Communication.Observe<NavigationStrategy> {

    override fun init(isFirsRun: Boolean) {
        if (isFirsRun) {
            navigationCommunication.map(NavigationStrategy.Replace(Screen.Numbers))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<NavigationStrategy>) {
        navigationCommunication.observe(owner, observer)
    }
}