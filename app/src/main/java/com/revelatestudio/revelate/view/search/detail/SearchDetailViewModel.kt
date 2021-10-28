package com.revelatestudio.revelate.view.search.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revelatestudio.revelate.data.repository.Repository
import com.revelatestudio.revelate.data.source.remote.NewsResponse
import com.revelatestudio.revelate.util.DispatcherProvider
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
}