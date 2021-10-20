package com.revelatestudio.revelate.data.repository

import com.revelatestudio.revelate.data.source.NewsReponse
import com.revelatestudio.revelate.util.Resource

interface Repository {

    suspend fun getTopHeadlinesByCountry() : Resource<NewsReponse>
    suspend fun getTopHeadlinesWithCategory(category : String) : Resource<NewsReponse>
    fun setDefaultCountryCode(countryCode: String)

}