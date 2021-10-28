package com.revelatestudio.revelate.view.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.revelatestudio.revelate.data.source.remote.ArticleItem
import com.revelatestudio.revelate.databinding.ItemNewsBinding
import com.revelatestudio.revelate.util.*

class ViewHolder(private val binding: ItemNewsBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

    fun bind(articleItem: ArticleItem?) {
        if (articleItem != null) {
            binding.apply {
                imgFeature.loadImage(articleItem.urlToImage, getProgressDrawable(context))
                tvTitle.text = articleItem.title
                tvBody.text = articleItem.description
                tvSource.text = "${articleItem.author ?: "Revelate News"} - ${getRelativeTimeSpanString(articleItem.publishedAt)}" ?: "Revelate News"

                articleItem.description ?: tvBody.gone()
                articleItem.urlToImage ?: imgFeature.gone()
                articleItem.title ?: tvTitle.gone()
            }

        }
    }
}