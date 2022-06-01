package com.depromeet.baton.presentation.di

import com.depromeet.baton.data.remote.datasource.TicketDataSource
import com.depromeet.baton.data.repository.TicketRepositoryImpl
import com.depromeet.baton.domain.repository.TicketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTicketRepository(ticketDataSource: TicketDataSource): TicketRepository {
        return TicketRepositoryImpl(ticketDataSource)
    }
}