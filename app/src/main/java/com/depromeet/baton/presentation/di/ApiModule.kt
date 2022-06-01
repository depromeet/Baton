package com.depromeet.baton.presentation.di

import com.depromeet.baton.data.remote.api.ticket.TicketService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideTicketService(retrofit: Retrofit): TicketService {
        return retrofit.create(TicketService::class.java)
    }
}