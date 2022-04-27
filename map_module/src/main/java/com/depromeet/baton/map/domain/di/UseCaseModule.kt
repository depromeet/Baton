package com.depromeet.baton.map.domain.di

import com.depromeet.baton.map.domain.repository.AddressRepository
import com.depromeet.baton.map.domain.usecase.GetAddressUseCase
/*import com.depromeet.baton.map.domain.repository.GPSRepository
import com.depromeet.baton.map.domain.usecase.GetLocationUseCase*/

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetAddressUseCase(repository: AddressRepository) = GetAddressUseCase(repository)
}