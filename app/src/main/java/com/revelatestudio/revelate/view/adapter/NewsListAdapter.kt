package com.revelatestudio.revelate.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.revelatestudio.revelate.data.source.remote.ArticleItem
import com.revelatestudio.revelate.databinding.ItemNewsBinding

class NewsListAdapter(private val onItemClick : (ArticleItem) -> Unit) :
   ListAdapter<ArticleItem , ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articlesItem = currentList[position]
        holder.itemView.setOnClickListener {
            onItemClick.invoke(articlesItem)
        }
        holder.bind(articlesItem)
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
}