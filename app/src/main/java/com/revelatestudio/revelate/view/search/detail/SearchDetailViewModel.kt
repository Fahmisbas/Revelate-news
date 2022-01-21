package com.revelatestudio.revelate.view.search.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revelatestudio.revelate.data.repository.Repository
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.data.dataholder.SearchHistory
import com.revelatestudio.revelate.data.source.remote.NewsResponse
import com.revelatestudio.revelate.util.DispatcherProvider
import com.revelatestudio.revelate.util.ERR_MSG
import com.revelatestudio.revelate.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchDetailViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatcher: DispatcherProvider
): ViewModel() {

    fun getNewsWithUserKeyword(keyword : String) : LiveData<Resource<NewsResponse>> {
        val mutableNewsWithUserKeyword = MutableLiveData<Resource<NewsResponse>>()
        val newsWithUserKeyword : LiveData<Resource<NewsResponse>> = mutableNewsWithUserKeyword
        viewModelScope.launch(dispatcher.io) {
            when (val response = repository.getNewsWithUserKeyword(keyword)) {
                is Resource.Error -> mutableNewsWithUserKeyword.postValue(Resource.Error("Unexpected error"))
                is Resource.Success -> mutableNewsWithUserKeyword.postValue(response)
            }
        }
        return newsWithUserKeyword
    }

    fun getItemNews(title : String) : LiveData<News>{
        val isNewsExist = MutableLiveData<News>()
        viewModelScope.launch(dispatcher.io) {
            when(val news = repository.getItemNews(title)) {
                is Resource.Success -> {
                    isNewsExist.postValue(news.data)
                }
                else -> {
                    isNewsExist.postValue(null)
                }
            }
        }
        return isNewsExist
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

    fun insertSearchHistory(searchHistory: SearchHistory) {
        viewModelScope.launch(dispatcher.io) {
            repository.insertSearchHistory(searchHistory)
        }
    }

    fun getSearchHistories() : LiveData<Resource<List<SearchHistory>>>{
        val mutableSearchHistories = MutableLiveData<Resource<List<SearchHistory>>>()
        viewModelScope.launch(dispatcher.io) {
            when(val searchHistories = repository.getSearchHistories()) {
                is Resource.Success -> mutableSearchHistories.postValue(searchHistories)
                is Resource.Error -> mutableSearchHistories.postValue(Resource.Error(searchHistories.message ?: ERR_MSG))
                else -> {
                    mutableSearchHistories.postValue(Resource.Empty())
                }
            }
        }
        return mutableSearchHistories
    }

    fun deleteSearchHistories() {
        viewModelScope.launch(dispatcher.io) {
            repository.deleteSearchHistories()
        }
    }
}