package com.revelatestudio.revelate.view.search

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

}