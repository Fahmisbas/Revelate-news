package com.revelatestudio.revelate.data.repository

import com.revelatestudio.revelate.data.source.remote.NewsResponse
import com.revelatestudio.revelate.util.Resource

interface Repository {

    suspend fun getTopHeadlinesByCountry() : Resource<NewsResponse>
    suspend fun getTopHeadlinesByCountryWithCategory(category : String) : Resource<NewsResponse>
    suspend fun getNewsWithUserKeyword(keyword : String) : Resource<NewsResponse>

    fun setDefaultCountryCode(countryCode: String)

}