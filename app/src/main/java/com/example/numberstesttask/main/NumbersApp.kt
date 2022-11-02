package com.example.numberstesttask.main

import android.app.Application
import com.example.numberstesttask.BuildConfig
import com.example.numberstesttask.numbers.data.cloud.CloudModule
import kotlinx.coroutines.DelicateCoroutinesApi

class NumbersApp : Application() {

    @DelicateCoroutinesApi
    override fun onCreate() {
        super.onCreate()

        //todo move out of here
        val cloudModule = if (BuildConfig.DEBUG)
            CloudModule.Debug()
        else
            CloudModule.Release()
    }
}