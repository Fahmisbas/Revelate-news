package com.revelatestudio.revelate.view.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revelatestudio.revelate.data.repository.MainRepository
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedArticleViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel(){

    fun getSavedNews() : LiveData<List<News>?> {
        val news = MutableLiveData<List<News>>()
        viewModelScope.launch(dispatcher.io) {
            news.postValue(repository.getSavedNews().data)
        }
        return news
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


}