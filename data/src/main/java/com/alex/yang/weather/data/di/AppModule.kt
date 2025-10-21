package com.alex.yang.weather.data.di

import android.content.Context
import com.alex.yang.weather.core.network.NetworkManager
import com.alex.yang.weather.data.local.AppPreferences
import com.alex.yang.weather.data.repository.ConfigRepository
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Created by AlexYang on 2025/10/18.
 *
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkManager(@ApplicationContext context: Context): NetworkManager =
        NetworkManager(context)

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig =
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 0
                }
            )
        }

    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context, json: Json): AppPreferences =
        AppPreferences(context, json)

    @Provides
    @Singleton
    fun provideConfigRepository(
        @ApplicationContext context: Context,
        remoteConfig: FirebaseRemoteConfig,
        preferences: AppPreferences,
        json: Json
    ): ConfigRepository = ConfigRepository(
        context = context,
        remoteConfig = remoteConfig,
        preferences = preferences,
        json = json
    )
}