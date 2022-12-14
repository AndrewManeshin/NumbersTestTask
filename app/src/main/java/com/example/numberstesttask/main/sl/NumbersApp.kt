package com.example.numberstesttask.main.sl

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.numberstesttask.BuildConfig
import com.example.numberstesttask.numbers.data.cloud.CloudModule

class NumbersApp : Application(), ProvideViewModel {

    private lateinit var viewModelsFactory: ViewModelsFactory

    override fun onCreate() {
        super.onCreate()

        val instances = if (BuildConfig.DEBUG)
            ProvideInstances.Mock(this)
        else
            ProvideInstances.Release(this)

        viewModelsFactory =
            ViewModelsFactory(
                DependencyContainer.Base(
                    Core.Base(
                        this,
                        instances
                    )
                )
            )
    }

    override fun <T : ViewModel> provideViewModel(clazz: Class<T>, owner: ViewModelStoreOwner): T =
        ViewModelProvider(owner, viewModelsFactory)[clazz]
}