package com.revelatestudio.revelate.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revelatestudio.revelate.data.repository.Repository
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.data.source.remote.NewsResponse
import com.revelatestudio.revelate.util.DispatcherProvider
import com.revelatestudio.revelate.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {


    fun getTopHeadlinesByCountry(): LiveData<Resource<NewsResponse>> {
        val mutableHeadlinesWithCategory = MutableLiveData<Resource<NewsResponse>>(Resource.Empty())
        val headlinesWithCategory : LiveData<Resource<NewsResponse>> = mutableHeadlinesWithCategory
        viewModelScope.launch(dispatcher.io) {
            when(val response =  repository.getTopHeadlinesByCountry()) {
                is Resource.Error -> mutableHeadlinesWithCategory.postValue(Resource.Error("Unexpected Error"))
                is Resource.Success -> mutableHeadlinesWithCategory.postValue(response)
            }
        }
        return headlinesWithCategory
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