package com.revelatestudio.revelate.data.repository

import com.revelatestudio.revelate.data.source.remote.NewsResponse
import com.revelatestudio.revelate.util.Resource

interface Repository {

    suspend fun getTopHeadlinesByCountry() : Resource<NewsResponse>
    suspend fun getTopHeadlinesWithCategory(category : String) : Resource<NewsResponse>
    fun setDefaultCountryCode(countryCode: String)

}