package com.alex.yang.weather.data.api

import com.alex.yang.weather.data.model.TimelineDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by AlexYang on 2025/10/15.
 *
 *
 */
interface VisualCrossingWebServices {
    /**
     * https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Taipei,TW/2025-10-13/2025-10-19?unitGroup=metric&include=hours,current&lang=zh&key=XXX
     */
    @GET("/VisualCrossingWebServices/rest/services/timeline/{location}/{startDay}/{endDay}")
    suspend fun getWeather(
        @Path("location", encoded = true) location: String = "Taipei,TW",
        @Path("startDay") startDay: String,
        @Path("endDay") endDay: String,
        @Query("unitGroup") unitGroup: String = "metric",
        @Query("include") include: String = "hours,current",
        @Query("lang") lang: String = "zh",
        @Query("key") key: String
    ): Response<TimelineDto>
}