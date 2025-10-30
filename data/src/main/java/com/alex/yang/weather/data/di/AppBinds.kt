package com.alex.yang.weather.data.di

import com.alex.yang.weather.data.datasource.AssetsLocalDataSource
import com.alex.yang.weather.data.datasource.AssetsLocalDataSourceImpl
import com.alex.yang.weather.data.datasource.RemoteConfigDataSource
import com.alex.yang.weather.data.datasource.RemoteConfigDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by AlexYang on 2025/10/30.
 *
 *
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppBinds {
    @Binds
    @Singleton
    abstract fun bindRemoteConfigDataSource(impl: RemoteConfigDataSourceImpl): RemoteConfigDataSource

    @Binds
    @Singleton
    abstract fun bindAssetsLocalDataSource(impl: AssetsLocalDataSourceImpl): AssetsLocalDataSource
}