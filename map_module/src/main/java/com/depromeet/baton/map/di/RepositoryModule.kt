package com.depromeet.baton.map.di

import com.depromeet.baton.map.data.dataSource.GPSDataSource
import com.depromeet.baton.map.data.dataSource.SearchDataSource
import com.depromeet.baton.map.data.repositoryImpl.AddressRepositoryImpl
import com.depromeet.baton.map.data.repositoryImpl.SearchAddressRepositoryImpl
import com.depromeet.baton.map.data.repositoryImpl.SearchShopRepositoryImpl
/*import com.depromeet.baton.map.data.repositoryImpl.GPSRepositoryImpl
import com.depromeet.baton.map.domain.repository.GPSRepository*/
import com.depromeet.baton.map.domain.repository.AddressRepository
import com.depromeet.baton.map.domain.repository.SearchAddressRepository
import com.depromeet.baton.map.domain.repository.SearchShopRepository

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
    fun provideAddressRepository( gpsDataSource: GPSDataSource ): AddressRepository {
        return AddressRepositoryImpl(gpsDataSource )
    }

    @Provides
    @Singleton
    fun provideSearchAddressRepository( dataSource: SearchDataSource): SearchAddressRepository {
        return SearchAddressRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideSearchShopRepository( dataSource: SearchDataSource): SearchShopRepository {
        return SearchShopRepositoryImpl(dataSource)
    }
}

