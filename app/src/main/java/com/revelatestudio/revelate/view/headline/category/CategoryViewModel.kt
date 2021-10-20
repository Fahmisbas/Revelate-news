package com.revelatestudio.revelate.view.headline.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revelatestudio.revelate.data.repository.Repository
import com.revelatestudio.revelate.data.source.NewsReponse
import com.revelatestudio.revelate.util.DispatcherProvider
import androidx.lifecycle.viewModelScope
import com.revelatestudio.revelate.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatcher: DispatcherProvider
) : ViewModel(){


    private val _headlinesWithCategory = MutableLiveData<Resource<NewsReponse>>(Resource.Empty())
    val headlinesWithCategory : LiveData<Resource<NewsReponse>> = _headlinesWithCategory

    fun setDefaultCountryCode(countryCode : String) {
        repository.setDefaultCountryCode(countryCode)
    }

    fun getTopHeadlinesByCountryWithCategory(category : String) {
        viewModelScope.launch(dispatcher.io) {
            when(val response =  repository.getTopHeadlinesWithCategory(category)) {
                is Resource.Error -> {
                    _headlinesWithCategory.postValue(Resource.Error("Unexpected Error"))
                }
                is Resource.Success -> {
                    _headlinesWithCategory.postValue(response)
                }
            }
        }
    }

    fun getTopHeadlinesByCountry() {
        viewModelScope.launch(dispatcher.io) {
            when(val response =  repository.getTopHeadlinesByCountry()) {
                is Resource.Error -> {
                    _headlinesWithCategory.postValue(Resource.Error("Unexpected Error"))
                }
                is Resource.Success -> {
                    _headlinesWithCategory.postValue(response)
                }
            }
        }
    }
}