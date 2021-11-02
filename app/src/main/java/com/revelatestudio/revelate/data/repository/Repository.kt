package com.revelatestudio.revelate.data.repository

import com.revelatestudio.revelate.data.source.local.News
import com.revelatestudio.revelate.data.source.remote.ArticleItem
import com.revelatestudio.revelate.data.source.remote.NewsResponse
import com.revelatestudio.revelate.util.Resource

interface Repository {

    suspend fun getTopHeadlinesByCountry() : Resource<NewsResponse>
    suspend fun getTopHeadlinesByCountryWithCategory(category : String) : Resource<NewsResponse>
    suspend fun getNewsWithUserKeyword(keyword : String) : Resource<NewsResponse>

    suspend fun getSavedNews() : Resource<List<News>>
    suspend fun insertNews(news: News) : Long
    suspend fun deleteNews(news: News)

    suspend fun getItemNews(title : String) : Resource<News>

    fun setDefaultCountryCode(countryCode: String)

}