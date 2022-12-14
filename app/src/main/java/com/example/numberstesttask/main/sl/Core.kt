package com.example.numberstesttask.main.sl

import android.content.Context
import com.example.numberstesttask.details.data.NumberFactDetails
import com.example.numberstesttask.main.presentaion.NavigationCommunication
import com.example.numberstesttask.numbers.data.cache.CacheModule
import com.example.numberstesttask.numbers.data.cloud.CloudModule
import com.example.numberstesttask.numbers.presentaion.DispatchersList
import com.example.numberstesttask.numbers.presentaion.ManageResources

interface Core : CloudModule, CacheModule, ManageResources, ProvideNavigation, ProvideNumberDetails {

    fun provideDispatchers(): DispatchersList

    class Base(
        context: Context,
        private val instances: ProvideInstances
    ) : Core {

        private val manageResources = ManageResources.Base(context)

        private val numberDetails = NumberFactDetails.Base()

        private val navigationCommunication = NavigationCommunication.Base()

        private val dispatchersList by lazy {
            DispatchersList.Base()
        }
        private val cloudModule by lazy {
            instances.provideCloudModule()
        }

        private val cacheModule by lazy {
            instances.provideCacheModule()
        }

        override fun <T> service(clazz: Class<T>): T = cloudModule.service(clazz)

        override fun provideDatabase() = cacheModule.provideDatabase()

        override fun string(id: Int) = manageResources.string(id)

        override fun provideDispatchers() = dispatchersList

        override fun provideNavigation() = navigationCommunication

        override fun provideNumberDetails(): NumberFactDetails.Mutable  = numberDetails
    }
}

interface ProvideNavigation {
    fun provideNavigation(): NavigationCommunication.Mutable
}

interface ProvideNumberDetails {
    fun provideNumberDetails() : NumberFactDetails.Mutable
}