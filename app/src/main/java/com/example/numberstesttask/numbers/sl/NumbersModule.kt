package com.example.numberstesttask.numbers.sl

import com.example.numberstesttask.main.sl.Core
import com.example.numberstesttask.main.sl.Module
import com.example.numberstesttask.numbers.data.BaseNumbersRepository
import com.example.numberstesttask.numbers.data.HandleDataRequest
import com.example.numberstesttask.numbers.data.HandleDomainError
import com.example.numberstesttask.numbers.data.NumberDataToDomain
import com.example.numberstesttask.numbers.data.cache.NumberDataToCache
import com.example.numberstesttask.numbers.data.cache.NumbersCacheDataSource
import com.example.numberstesttask.numbers.data.cloud.NumbersCloudDataSource
import com.example.numberstesttask.numbers.data.cloud.NumbersService
import com.example.numberstesttask.numbers.domain.HandleError
import com.example.numberstesttask.numbers.domain.HandleRequest
import com.example.numberstesttask.numbers.domain.NumberUiMapper
import com.example.numberstesttask.numbers.domain.NumbersInteractor
import com.example.numberstesttask.numbers.presentaion.*

class NumbersModule(
    private val core: Core
) : Module<NumbersViewModel.Base> {

    override fun viewModel(): NumbersViewModel.Base {
        val communication = NumbersCommunications.Base(
            ProgressCommunication.Base(),
            NumbersStateCommunication.Base(),
            NumbersListCommunication.Base()
        )

        val cacheDataSource = NumbersCacheDataSource.Base(
            core.provideDatabase().numbersDao(),
            NumberDataToCache()
        )

        val mapperToDomain = NumberDataToDomain()

        val repository = BaseNumbersRepository(
            cacheDataSource,
            NumbersCloudDataSource.Base(
                core.service(NumbersService::class.java)
            ),
            HandleDataRequest.Base(
                HandleDomainError(),
                cacheDataSource,
                mapperToDomain
            ),
            mapperToDomain
        )

        return NumbersViewModel.Base(
            HandleNumbersRequest.Base(
                core.provideDispatchers(),
                communication,
                NumbersResultMapper(
                    communication,
                    NumberUiMapper()
                )
            ),
            core,
            communication,
            NumbersInteractor.Base(
                repository,
                HandleRequest.Base(
                    HandleError.Base(core),
                    repository
                ),
                core.provideNumberDetails()
            ),
            core.provideNavigation(),
            DetailsUi()
        )
    }
}