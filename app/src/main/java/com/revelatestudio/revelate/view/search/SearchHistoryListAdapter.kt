package com.revelatestudio.revelate.view.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.revelatestudio.revelate.data.dataholder.SearchHistory
import com.revelatestudio.revelate.databinding.ItemSearchHistoryBinding

class SearchHistoryListAdapter(private val onItemClick : (SearchHistory) -> Unit) : ListAdapter<SearchHistory, SearchHistoryListAdapter.ViewHolder>(
    DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchHistoryListAdapter.ViewHolder {
        val binding = ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHistoryListAdapter.ViewHolder, position: Int) {
        val item = currentList[position]
        holder.itemView.setOnClickListener {
            onItemClick.invoke(item)
        }
        holder.bind(item)
    }


    inner class ViewHolder(private val binding: ItemSearchHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchHistory?) {
            binding.tvKeyword.text = item?.keyword
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchHistory>() {
            override fun areItemsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
                return oldItem.id == newItem.id
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
                return oldItem == newItem
            }
        }
    }


}