package com.example.numberstesttask.main.presentaion

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

interface NavigationStrategy {

    fun navigate(manager: FragmentManager, containerId: Int)

    abstract class Abstract(protected val fragment: Fragment) : NavigationStrategy {

        override fun navigate(manager: FragmentManager, containerId: Int) {
            manager.beginTransaction()
                .executeTransaction(containerId)
                .commit()
        }

        protected abstract fun FragmentTransaction.executeTransaction(
            containerId: Int
        ): FragmentTransaction
    }

    class Replace(fragment: Fragment) : Abstract(fragment) {

        override fun FragmentTransaction.executeTransaction(containerId: Int) =
            replace(containerId, fragment)
    }

    class Add(fragment: Fragment) : Abstract(fragment) {

        override fun FragmentTransaction.executeTransaction(containerId: Int) =
            add(containerId, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
    }
}