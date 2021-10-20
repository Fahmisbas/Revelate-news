package com.revelatestudio.revelate.data.source.remote

import com.revelatestudio.revelate.data.source.NewsReponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


private const val API_KEY =  "your api key"

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesByCountry(
        @Query("country") country: String? = "us",
        @Query("apiKey") apiKey: String? = API_KEY,
    ) : Response<NewsReponse>

    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesWithCategory(
        @Query("country") country: String? = "us",
        @Query("category") category: String?,
        @Query("apiKey") apiKey: String? =  API_KEY
    ) : Response<NewsReponse>

}