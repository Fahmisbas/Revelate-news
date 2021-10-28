package com.revelatestudio.revelate.view.headline.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revelatestudio.revelate.data.repository.Repository
import com.revelatestudio.revelate.data.source.remote.NewsResponse
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

    fun setDefaultCountryCode(countryCode : String) {
        repository.setDefaultCountryCode(countryCode)
    }

    fun getTopHeadlinesByCountryWithCategory(category : String) : LiveData<Resource<NewsResponse>> {
        val mutableHeadlinesByCountryWithCategory = MutableLiveData<Resource<NewsResponse>>(Resource.Empty())
        val headlinesByCountryWithCategory : LiveData<Resource<NewsResponse>> = mutableHeadlinesByCountryWithCategory
        viewModelScope.launch(dispatcher.io) {
            when(val response =  repository.getTopHeadlinesByCountryWithCategory(category)) {
                is Resource.Error -> {
                    mutableHeadlinesByCountryWithCategory.postValue(Resource.Error("Unexpected Error"))
                }
                is Resource.Success -> {
                    mutableHeadlinesByCountryWithCategory.postValue(response)
                }
            }
        }
        return headlinesByCountryWithCategory
    }
}