package com.revelatestudio.revelate.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.revelatestudio.revelate.data.source.remote.ArticlesItem
import com.revelatestudio.revelate.databinding.ItemNewsBinding
import com.revelatestudio.revelate.util.getProgressDrawable
import com.revelatestudio.revelate.util.gone
import com.revelatestudio.revelate.util.loadImage


class NewsListAdapter(private val onItemClick : (ArticlesItem) -> Unit) :
   ListAdapter<ArticlesItem ,NewsListAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var _context: Context? = null
    private val context get() = _context!!

    fun setContext(context: Context) {
        _context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articlesItem = currentList[position]
        holder.itemView.setOnClickListener {
            onItemClick.invoke(articlesItem)
        }
        holder.bind(articlesItem)
    }

    override fun getItemCount() = currentList.size

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(articlesItem: ArticlesItem?) {

            if (articlesItem != null) {
                binding.apply {
                    imgFeature.loadImage(articlesItem.urlToImage, getProgressDrawable(context))
                    tvTitle.text = articlesItem.title
                    tvBody.text = articlesItem.description
                    tvSource.text = articlesItem.author ?: "Revelate News"
                }
                articlesItem.description ?: binding.tvBody.gone()
                articlesItem.urlToImage ?: binding.imgFeature.gone()
                articlesItem.title ?: binding.tvTitle.gone()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem.content == newItem.content
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}