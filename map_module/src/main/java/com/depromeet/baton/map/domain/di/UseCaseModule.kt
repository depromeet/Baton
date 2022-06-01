package com.depromeet.baton.map.domain.di

import com.depromeet.baton.map.domain.repository.AddressRepository
import com.depromeet.baton.map.domain.repository.SearchAddressRepository
import com.depromeet.baton.map.domain.repository.SearchShopRepository
import com.depromeet.baton.map.domain.usecase.GetAddressUseCase
import com.depromeet.baton.map.domain.usecase.SearchAddressUseCase
import com.depromeet.baton.map.domain.usecase.SearchShopUseCase

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


    @Provides
    @Singleton
    fun provideSearchAddressUseCase(repository: SearchAddressRepository) = SearchAddressUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchShopUseCase(repository: SearchShopRepository) = SearchShopUseCase(repository)

}