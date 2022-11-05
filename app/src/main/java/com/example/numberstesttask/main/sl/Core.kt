package com.example.numberstesttask.main.sl

import android.content.Context
import com.example.numberstesttask.numbers.data.cache.CacheModule
import com.example.numberstesttask.numbers.data.cache.NumbersDatabase
import com.example.numberstesttask.numbers.data.cloud.CloudModule
import com.example.numberstesttask.numbers.presentaion.DispatchersList
import com.example.numberstesttask.numbers.presentaion.ManageResources

interface Core : CloudModule, CacheModule, ManageResources {

    fun provideDispatchers(): DispatchersList

    class Base(
        context: Context,
        private val isRelease: Boolean
    ) : Core {

        private val manageResources = ManageResources.Base(context)

        private val dispatchersList by lazy {
            DispatchersList.Base()
        }
        private val cloudModule by lazy {
            if (isRelease)
                CloudModule.Debug()
            else
                CloudModule.Release()
        }

        private val cacheModule by lazy {
            if (isRelease)
                CacheModule.Base(context)
            else
                CacheModule.Mock(context)
        }

        override fun <T> service(clazz: Class<T>): T = cloudModule.service(clazz)

        override fun provideDatabase() = cacheModule.provideDatabase()

        override fun string(id: Int) = manageResources.string(id)

        override fun provideDispatchers() = dispatchersList
    }
}