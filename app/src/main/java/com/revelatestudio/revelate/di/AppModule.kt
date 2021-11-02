package com.revelatestudio.revelate.di

import android.content.Context
import androidx.room.Room
import com.revelatestudio.revelate.data.repository.MainRepository
import com.revelatestudio.revelate.data.repository.Repository
import com.revelatestudio.revelate.data.source.local.NewsDao
import com.revelatestudio.revelate.data.source.local.NewsDatabase
import com.revelatestudio.revelate.data.source.remote.NewsApi
import com.revelatestudio.revelate.util.DispatcherProvider
import com.revelatestudio.revelate.util.NEWS_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://newsapi.org/"

@Module
@InstallIn(SingletonComponent::class) // Global Scope
object AppModule {



    @Singleton
    @Provides
    fun provideNewsDatabase(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app,
        NewsDatabase::class.java,
        NEWS_DATABASE_NAME
    ).build()


    @Singleton
    @Provides
    fun provideNewsDao(db : NewsDatabase) = db.getNewsDao()


    @Singleton
    @Provides
    fun provideNewsApi(): NewsApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: NewsApi, newsDao : NewsDao) : Repository = MainRepository(api, newsDao)


    @Singleton
    @Provides
    fun provideDispatcher(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}