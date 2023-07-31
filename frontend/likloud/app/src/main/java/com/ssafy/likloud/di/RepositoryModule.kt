package com.ssafy.likloud.di

import com.ssafy.likloud.config.BearerInterceptor
import com.ssafy.likloud.data.api.BaseService
import com.ssafy.likloud.data.repository.BaseRepository
import com.ssafy.likloud.data.repository.BaseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(baseAPIService: BaseService): BaseRepository {
        return BaseRepositoryImpl(baseAPIService)
    }

//    @Singleton
//    @Provides
//    fun provideBearerInterceptor(baseRepository: BaseRepository): BearerInterceptor {
//        return BearerInterceptor(baseRepository)
//    }
}