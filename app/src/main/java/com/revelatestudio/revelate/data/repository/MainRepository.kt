package com.revelatestudio.revelate.data.repository

import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.data.dataholder.SearchHistory
import com.revelatestudio.revelate.data.source.local.NewsDao
import com.revelatestudio.revelate.data.source.remote.NewsResponse
import com.revelatestudio.revelate.data.source.remote.NewsApi
import com.revelatestudio.revelate.util.ERR_MSG
import com.revelatestudio.revelate.util.Resource
import com.revelatestudio.revelate.util.SERVER_ERR_MSG
import java.lang.Exception
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api: NewsApi,
    private val dao : NewsDao
) : Repository {

    private var defaultCountryCode : String? = null

    override fun setDefaultCountryCode(countryCode: String) {
        defaultCountryCode = countryCode
    }

    override suspend fun getTopHeadlinesByCountry(): Resource<NewsResponse> {
        return try {
            val response = api.getTopHeadlinesByCountry(defaultCountryCode)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: SERVER_ERR_MSG)
        }
    }

    override suspend fun getTopHeadlinesByCountryWithCategory(category: String): Resource<NewsResponse> {
        return try {
            val response = api.getTopHeadlinesByCountryWithCategory(defaultCountryCode, category)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: SERVER_ERR_MSG)
        }
    }

    override suspend fun getNewsWithUserKeyword(keyword: String): Resource<NewsResponse> {
        return try {
            val response = api.getNewsWithUserKeyword(keyword)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: SERVER_ERR_MSG)
        }
    }

    override suspend fun getSavedNews(): Resource<List<News>> {
        return try {
            val news = dao.getAllNews()
            if (news.isNotEmpty()) {
                Resource.Success(news)
            } else Resource.Empty()
        } catch (e : Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: ERR_MSG)
        }
    }

    override suspend fun insertNews(news: News) : Long  {
        return try{
            dao.insertNews(news)
        } catch (e : Exception) {
            e.printStackTrace()
            -1
        }
    }

    override suspend fun deleteNews(news: News) {
        try {
            dao.deleteNews(news)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getItemNews(title: String): Resource<News> {
        val item = dao.getItemNews(title)
        return if(item?.id != null){
            Resource.Success(item)
        } else {
            Resource.Empty()
        }
    }

    override suspend fun insertSearchHistory(searchHistory: SearchHistory): Long {
        return try {
            dao.insertSearchHistory(searchHistory)
        } catch (e : Exception) {
            e.printStackTrace()
            -1
        }
    }


    override suspend fun getSearchHistories(): Resource<List<SearchHistory>>  {
        return try {
            val searchHistories = dao.getSearchHistories()
            if (searchHistories.isNotEmpty()) {
                Resource.Success(searchHistories)
            } else Resource.Empty()
        } catch (e : Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: ERR_MSG)
        }
    }


    override suspend fun deleteSearchHistories() {
        try {
            dao.deleteSearchHistories()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }


}