package com.depromeet.baton.util

import android.content.Context

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
class SharedPreferenceManagerDI {
    @Provides
    fun provideSharedPreference(@ApplicationContext context : Context)
            =BatonSpfManager(context)
}
