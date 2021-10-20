package com.revelatestudio.revelate.data.repository

import com.revelatestudio.revelate.data.source.NewsReponse
import com.revelatestudio.revelate.data.source.remote.NewsApi
import com.revelatestudio.revelate.util.Resource
import java.lang.Exception
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api : NewsApi
) : Repository {

    private var countryCode = "us"

    override fun setDefaultCountryCode(countryCode: String) {
        this.countryCode = countryCode
    }

    override suspend fun getTopHeadlinesByCountry(): Resource<NewsReponse> {
        return try {
            val response = api.getTopHeadlinesByCountry(countryCode)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e : Exception) {
            Resource.Error(e.message ?: "an error occurred. Failed to retrieve news")
        }
    }

    override suspend fun getTopHeadlinesWithCategory(category: String): Resource<NewsReponse> {
        return try {
            val response = api.getTopHeadlinesWithCategory(countryCode, category)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e : Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }



}