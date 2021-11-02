package com.revelatestudio.revelate.view.adapter

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.data.source.local.News
import com.revelatestudio.revelate.data.source.remote.ArticleItem
import com.revelatestudio.revelate.databinding.ItemNewsBinding
import com.revelatestudio.revelate.util.*

class ViewHolder(
    private val binding: ItemNewsBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    private var toggle = false

    fun newsToObserve(articleItem: ArticleItem?, news: LiveData<News>?, lifecycleOwner : LifecycleOwner?) {
        articleItem?.title?.let {
            if (lifecycleOwner != null) {
                news?.observe(lifecycleOwner, { news ->
                    if (news != null) {
                        articleItem.id = news.id
                        binding.btnBookmark.setImageResource(R.drawable.ic_bookmarked)
                        toggle = true
                    }
                })
            }
        }
    }

    fun bind(articleItem: ArticleItem?, toggleSave: (Boolean, ArticleItem) -> Unit) {
        if (articleItem != null) {
            binding.apply {
                with(articleItem) {
                    imgFeature.loadImage(urlToImage, getProgressDrawable(context))
                    tvTitle.text = title
                    tvBody.text = description
                    tvSource.text = "${articleItem.author ?: "Revelate News"} - ${
                        getRelativeTimeSpanString(articleItem.publishedAt)
                    }" ?: "Revelate News"
                }

                btnBookmark.setOnClickListener {
                    toggle = !toggle
                    if (toggle) {
                        btnBookmark.setImageResource(R.drawable.ic_bookmarked)
                    } else btnBookmark.setImageResource(R.drawable.ic_bookmark)

                    toggleSave.invoke(toggle, articleItem)
                }


                with(articleItem) {
                    description ?: tvBody.gone()
                    urlToImage ?: imgFeature.gone()
                    title ?: tvTitle.gone()
                }
            }
        }
    }
}