package com.example.numberstesttask.main.presentaion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.example.numberstesttask.R
import com.example.numberstesttask.main.sl.ProvideViewModel
import com.example.numberstesttask.numbers.presentaion.NumbersFragment

class MainActivity : AppCompatActivity(), ShowFragment, ProvideViewModel {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            NavigationStrategy.Replace(NumbersFragment()).navigate(supportFragmentManager, R.id.container)
    }

    override fun show(fragment: Fragment) {
        NavigationStrategy.Add(fragment).navigate(supportFragmentManager, R.id.container)
    }

    override fun <T : ViewModel> provideViewModel(clazz: Class<T>, owner: ViewModelStoreOwner): T =
        (application as ProvideViewModel).provideViewModel(clazz, owner)
}

interface ShowFragment {
    fun show(fragment: Fragment)

    class Empty : ShowFragment {
        override fun show(fragment: Fragment) = Unit
    }
}