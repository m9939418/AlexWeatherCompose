package com.alex.yang.weather.data.di

import com.alex.yang.weather.data.repository.ConfigRepositoryImpl
import com.alex.yang.weather.data.datasource.DataStoreRepositoryImpl
import com.alex.yang.weather.data.repository.WeatherRepositoryImpl
import com.alex.yang.weather.domain.repository.ConfigRepository
import com.alex.yang.weather.domain.repository.DataStoreRepository
import com.alex.yang.weather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by AlexYang on 2025/10/17.
 *
 *
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBinds {
    @Binds
    @Singleton
    abstract fun bindsWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindDataStoreRepository(impl: DataStoreRepositoryImpl): DataStoreRepository

    @Binds
    @Singleton
    abstract fun bindConfigRepository(impl: ConfigRepositoryImpl): ConfigRepository

}