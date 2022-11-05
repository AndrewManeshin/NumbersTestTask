package com.example.numberstesttask.main.presentaion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.example.numberstesttask.R
import com.example.numberstesttask.details.presentation.DetailsFragment
import com.example.numberstesttask.main.sl.ProvideViewModel
import com.example.numberstesttask.numbers.presentaion.NumbersFragment

class MainActivity : AppCompatActivity(), ShowFragment, ProvideViewModel {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            show(NumbersFragment(), false)
    }

    override fun show(fragment: Fragment) {
        show(fragment, true)
    }

    private fun show(fragment: Fragment, add: Boolean) {
        //todo make OOP
        val transaction = supportFragmentManager.beginTransaction()
        val container = R.id.container
        if (add) {
            transaction.add(container, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
        } else {
            transaction.replace(container, NumbersFragment())
        }
        transaction.commit()
    }

    override fun <T : ViewModel> provideViewModel(clasz: Class<T>, owner: ViewModelStoreOwner): T =
        (application as ProvideViewModel).provideViewModel(clasz, owner)
}

interface ShowFragment {
    fun show(fragment: Fragment)

    class Empty : ShowFragment {
        override fun show(fragment: Fragment) = Unit
    }
}