package com.revelatestudio.revelate.data.repository

import com.revelatestudio.revelate.data.source.remote.NewsResponse
import com.revelatestudio.revelate.data.source.remote.NewsApi
import com.revelatestudio.revelate.util.Resource
import java.lang.Exception
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api : NewsApi
) : Repository {

    private var defaultCountryCode = "us"

    override fun setDefaultCountryCode(countryCode: String) {
        this.defaultCountryCode = countryCode
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
        } catch (e : Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "an error occurred. Failed to retrieve news")
        }
    }

    override suspend fun getTopHeadlinesWithCategory(category: String): Resource<NewsResponse> {
        return try {
            val response = api.getTopHeadlinesWithCategory(defaultCountryCode, category)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e : Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An error occurred")
        }
    }



}