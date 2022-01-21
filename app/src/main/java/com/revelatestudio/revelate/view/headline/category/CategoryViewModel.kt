package com.revelatestudio.revelate.view.headline.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revelatestudio.revelate.data.repository.Repository
import com.revelatestudio.revelate.data.source.remote.NewsResponse
import com.revelatestudio.revelate.util.DispatcherProvider
import androidx.lifecycle.viewModelScope
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatcher: DispatcherProvider
) : ViewModel(){

    fun setDefaultCountryCode(countryCode : String) {
        repository.setDefaultCountryCode(countryCode)
    }

    fun getTopHeadlinesByCountryWithCategory(category : String) : LiveData<Resource<NewsResponse>> {
        val headline = MutableLiveData<Resource<NewsResponse>>(Resource.Empty())
        viewModelScope.launch(dispatcher.io) {
            when(val response =  repository.getTopHeadlinesByCountryWithCategory(category)) {
                is Resource.Error -> {
                    headline.postValue(Resource.Error("Unexpected Error"))
                }
                is Resource.Success -> {
                    headline.postValue(response)
                }
            }
        }
        return headline
    }



    fun insertNews(news: News) : LiveData<Long>{
        val id = MutableLiveData<Long>()
        viewModelScope.launch(dispatcher.io) {
            val newsId = repository.insertNews(news)
            id.postValue(newsId)
        }
        return id
    }

    fun deleteNews(news: News) {
        viewModelScope.launch(dispatcher.io) {
            repository.deleteNews(news)
        }
    }

    fun getItemNews(title : String) : LiveData<News>{
        val isNewsExist = MutableLiveData<News>()
        viewModelScope.launch(dispatcher.io) {
            when(repository.getItemNews(title)) {
                is Resource.Success -> {
                    isNewsExist.postValue(repository.getItemNews(title).data)
                }
                else -> {
                    isNewsExist.postValue(null)
                }
            }
        }
        return isNewsExist
    }

}