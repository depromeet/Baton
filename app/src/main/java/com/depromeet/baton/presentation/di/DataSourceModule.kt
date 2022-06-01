package com.depromeet.baton.presentation.di

import com.depromeet.baton.data.remote.api.ticket.TicketService
import com.depromeet.baton.data.remote.datasource.TicketDataSource
import com.depromeet.baton.data.remote.datasource.TicketDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideTicketDataSource(ticketService: TicketService): TicketDataSource {
        return TicketDataSourceImpl(ticketService)
    }
}