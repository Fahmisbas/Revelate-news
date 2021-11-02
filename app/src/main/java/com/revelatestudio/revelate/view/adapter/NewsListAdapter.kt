package com.revelatestudio.revelate.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.revelatestudio.revelate.data.source.local.News
import com.revelatestudio.revelate.data.source.remote.ArticleItem
import com.revelatestudio.revelate.databinding.ItemNewsBinding

class NewsListAdapter(private val onItemClick : (ArticleItem) -> Unit, private val onSaveButtonClick : (Boolean, ArticleItem) -> Unit
                      , private var newsLiveData : LiveData<News>? = null, var lifecycleOwner: LifecycleOwner? = null) :
   ListAdapter<ArticleItem , ViewHolder>(DIFF_CALLBACK) {

    private var onGetArticlesItem : OnGetArticlesItem? = null

    fun newsToObserve(news : LiveData<News>, lifecycleOwner: LifecycleOwner) {
        this.newsLiveData = news
        this.lifecycleOwner = lifecycleOwner
    }

    fun setOnGetArticlesItem(onGetArticlesItem: OnGetArticlesItem) {
        this.onGetArticlesItem = onGetArticlesItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articlesItem = currentList[position]

        onGetArticlesItem?.onArticlesItem(articlesItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(articlesItem)
        }

        if (newsLiveData != null && lifecycleOwner != null) holder.newsToObserve(articlesItem, newsLiveData, lifecycleOwner)

        holder.bind(articlesItem) { toggleSave, newsItem  ->
            onSaveButtonClick.invoke(toggleSave, newsItem )
        }
    }

    override fun getItemCount() = currentList.size

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleItem>() {
            override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem.content == newItem.content
            }
            override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnGetArticlesItem {
        fun onArticlesItem(articleItem: ArticleItem?)
    }
}