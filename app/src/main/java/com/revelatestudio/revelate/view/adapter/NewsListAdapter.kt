package com.revelatestudio.revelate.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.data.source.remote.NewsItemResponse
import com.revelatestudio.revelate.databinding.ItemNewsBinding

class NewsListAdapter(private val onItemClick : (NewsItemResponse) -> Unit, private val onSaveButtonClick : (Boolean, NewsItemResponse) -> Unit
                      , private var newsLiveData : LiveData<News>? = null, var lifecycleOwner: LifecycleOwner? = null) :
   ListAdapter<NewsItemResponse , NewsViewHolder>(DIFF_CALLBACK) {

    private var onGetNewsItem : OnGetNewsItem? = null

    fun newsToObserve(news : LiveData<News>, lifecycleOwner: LifecycleOwner) {
        this.newsLiveData = news
        this.lifecycleOwner = lifecycleOwner
    }

    fun setOnGetArticlesItem(onGetNewsItem: OnGetNewsItem) {
        this.onGetNewsItem = onGetNewsItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding,parent.context)
    }

    override fun onBindViewHolder(holderNews: NewsViewHolder, position: Int) {
        val articlesItem = currentList[position]

        onGetNewsItem?.onArticlesItem(articlesItem)

        holderNews.itemView.setOnClickListener {
            onItemClick.invoke(articlesItem)
        }

        if (newsLiveData != null && lifecycleOwner != null) holderNews.newsToObserve(articlesItem, newsLiveData, lifecycleOwner)

        holderNews.bind(articlesItem) { toggleSave, newsItem  ->
            onSaveButtonClick.invoke(toggleSave, newsItem )
        }
    }

    override fun getItemCount() = currentList.size

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsItemResponse>() {
            override fun areItemsTheSame(oldItemResponse: NewsItemResponse, newItemResponse: NewsItemResponse): Boolean {
                return oldItemResponse.title == newItemResponse.title
            }
            override fun areContentsTheSame(oldItemResponse: NewsItemResponse, newItemResponse: NewsItemResponse): Boolean {
                return oldItemResponse.content == newItemResponse.content
            }
        }
    }

    interface OnGetNewsItem {
        fun onArticlesItem(newsItemResponse: NewsItemResponse?)
    }
}