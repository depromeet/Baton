package com.depromeet.baton.presentation.di



import com.depromeet.baton.annotation.Server
import com.depromeet.baton.annotation.ServerType
import com.depromeet.baton.remote.AuthNetworkInterceptor
import com.depromeet.baton.remote.search.SearchService
import com.depromeet.baton.remote.ticket.BookmarkService
import com.depromeet.baton.remote.ticket.InquiryService
import com.depromeet.baton.remote.ticket.TicketInfoService
import com.depromeet.baton.remote.user.SignService
import com.depromeet.baton.remote.user.TokenService
import com.depromeet.baton.remote.user.UserInfoService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authNetworkInterceptor: AuthNetworkInterceptor
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            this.setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        return OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(logging)
            .addNetworkInterceptor(authNetworkInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Server(ServerType.Search)
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit {
        return internalCreateRetrofit("https://baton.yonghochoi.com/search/", okHttpClient, moshi)
    }

    @Provides
    @Singleton
    @Server(ServerType.User)
    fun provideUserRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit {
        return internalCreateRetrofit("https://baton.yonghochoi.com/", okHttpClient, moshi)
    }


    @Provides
    @Singleton
    fun provideSignService(@Server(ServerType.User) retrofit: Retrofit): SignService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideSearchService(@Server(ServerType.Search) retrofit: Retrofit): SearchService {
        return retrofit.create()
    }


    @Provides
    @Singleton
    fun provideTicketInfoService(@Server(ServerType.Search) retrofit: Retrofit): TicketInfoService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideUserInfoService(@Server(ServerType.User) retrofit: Retrofit): UserInfoService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideBookmarkService(@Server(ServerType.User) retrofit: Retrofit): BookmarkService {
        return retrofit.create()
    }


    @Provides
    @Singleton
    fun provideInquiryService(@Server(ServerType.Search) retrofit: Retrofit): InquiryService {
        return retrofit.create()
    }


    @Provides
    @Singleton
    fun provideAuthTokenService(@Server(ServerType.User) retrofit: Retrofit): TokenService {
        return retrofit.create()
    }

    private fun internalCreateRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}