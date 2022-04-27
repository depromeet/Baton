package com.depromeet.baton.map.di

import com.depromeet.baton.map.data.dataSource.GPSDataSource
import com.depromeet.baton.map.data.repositoryImpl.AddressRepositoryImpl
/*import com.depromeet.baton.map.data.repositoryImpl.GPSRepositoryImpl
import com.depromeet.baton.map.domain.repository.GPSRepository*/
import com.depromeet.baton.map.domain.repository.AddressRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAddressRepository( dataSource: GPSDataSource): AddressRepository {
        return AddressRepositoryImpl(dataSource)
    }
}

