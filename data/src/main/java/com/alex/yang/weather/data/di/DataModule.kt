package com.alex.yang.weather.data.di

import com.alex.yang.weather.data.api.VisualCrossingWebServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Created by AlexYang on 2025/10/16.
 *
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    @AppHost
    fun provideBaseUrl(): String = "https://weather.visualcrossing.com"

    @Provides
    @Singleton
    @MockupHost
//    fun provideMockupUrl(): String = "https://9a47974b-22eb-4c30-b6a5-6a90e9b69bd4.mock.pstmn.io"
    fun provideMockupUrl(): String = "https://e82f7cbd-cb7a-4703-918a-056c84b15191.mock.pstmn.io"

    @Provides
    @Singleton
    fun provideKotlinJson() =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }

    @Provides
    @Singleton
    fun provideOkHttpClient() =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS).apply {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                )
                // TODO:using fake data
//                addInterceptor(MockServerInterceptor())
            }
            .build()

    @Provides
    @Singleton
    fun provideKotlinXFactory(json: Json): Converter.Factory =
        json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideRetrofit(
//        @AppHost baseUrl: String,
        @MockupHost baseUrl: String,
        client: OkHttpClient,
        factory: Converter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(factory)
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): VisualCrossingWebServices =
        retrofit.create(VisualCrossingWebServices::class.java)

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AppHost

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class MockupHost
}