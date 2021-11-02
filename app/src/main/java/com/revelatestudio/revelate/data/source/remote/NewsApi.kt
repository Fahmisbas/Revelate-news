package com.revelatestudio.revelate.data.source.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


private const val API_KEY = "Your API key"

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesByCountry(
        @Query("country") country: String? = "us",
        @Query("apiKey") apiKey: String? = API_KEY,
    ) : Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesByCountryWithCategory(
        @Query("country") country: String? = "us",
        @Query("category") category: String?,
        @Query("apiKey") apiKey: String? =  API_KEY
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getNewsWithUserKeyword(
        @Query("q") q : String?,
        @Query("apiKey") apiKey: String? = API_KEY
    ): Response<NewsResponse>
}