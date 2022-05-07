package com.depromeet.baton.map.di

import android.content.Context
import com.depromeet.baton.map.data.dataSource.GPSDataSource
import com.depromeet.baton.map.data.dataSource.SearchDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
 class DataSourceModule {
    @Provides
    fun provideGPSDataSource(@ApplicationContext context : Context)
    = GPSDataSource(context)

    @Provides
    fun provideSearchDataSource()  = SearchDataSource()

}

