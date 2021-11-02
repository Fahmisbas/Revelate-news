package com.revelatestudio.revelate.data.repository

import androidx.lifecycle.MutableLiveData
import com.revelatestudio.revelate.data.source.local.News
import com.revelatestudio.revelate.data.source.local.NewsDao
import com.revelatestudio.revelate.data.source.remote.NewsResponse
import com.revelatestudio.revelate.data.source.remote.NewsApi
import com.revelatestudio.revelate.util.Resource
import com.revelatestudio.revelate.util.ERR_MSG
import java.lang.Exception
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api: NewsApi,
    private val dao : NewsDao
) : Repository {

    private var defaultCountryCode = MutableLiveData<String>()

    override fun setDefaultCountryCode(countryCode: String) {
        defaultCountryCode.postValue(countryCode)
    }

    override suspend fun getTopHeadlinesByCountry(): Resource<NewsResponse> {
        return try {
            val response = api.getTopHeadlinesByCountry(defaultCountryCode.value)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: ERR_MSG)
        }
    }

    override suspend fun getTopHeadlinesByCountryWithCategory(category: String): Resource<NewsResponse> {
        return try {
            val response = api.getTopHeadlinesByCountryWithCategory(defaultCountryCode.value, category)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: ERR_MSG)
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
            Resource.Error(e.message ?: ERR_MSG)
        }
    }

    override suspend fun getSavedNews(): Resource<List<News>> {
        val news = dao.getAllNews()
        return if (news.isNotEmpty()) {
            Resource.Success(news)
        } else Resource.Empty()
    }

    override suspend fun insertNews(news: News) : Long  {
        return dao.insertNews(news)
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


}