package com.revelatestudio.revelate.view.adapter

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.data.source.remote.NewsItemResponse
import com.revelatestudio.revelate.databinding.ItemNewsBinding
import com.revelatestudio.revelate.util.*
import com.revelatestudio.revelate.util.ext.getProgressDrawable
import com.revelatestudio.revelate.util.ext.gone
import com.revelatestudio.revelate.util.ext.loadImage

class NewsViewHolder(
    private val binding: ItemNewsBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    private var toggle = false

    fun newsToObserve(newsItemResponse: NewsItemResponse?, news: LiveData<News>?, lifecycleOwner : LifecycleOwner?) {
        newsItemResponse?.title?.let {
            if (lifecycleOwner != null) {
                news?.observe(lifecycleOwner, { savedNews ->
                    if (savedNews != null) {
                        newsItemResponse.id = savedNews.id
                        binding.btnBookmark.setImageResource(R.drawable.ic_bookmarked)
                        toggle = true
                    }
                })
            }
        }
    }

    fun bind(newsItemResponse: NewsItemResponse?, toggleSave: (Boolean, NewsItemResponse) -> Unit) {
        if (newsItemResponse != null) {
            binding.apply {
                with(newsItemResponse) {
                    imgFeature.loadImage(urlToImage, getProgressDrawable(context))
                    tvTitle.text = title
                    tvBody.text = description
                    tvSource.text = "${author ?: "Revelate News"} - ${
                        getRelativeTimeSpanString(newsItemResponse.publishedAt)
                    }" ?: "Revelate News"
                }

                btnBookmark.setOnClickListener {
                    toggle = !toggle
                    if (toggle) {
                        btnBookmark.setImageResource(R.drawable.ic_bookmarked)
                    } else btnBookmark.setImageResource(R.drawable.ic_bookmark)

                    toggleSave.invoke(toggle, newsItemResponse)
                }


                with(newsItemResponse) {
                    description ?: tvBody.gone()
                    urlToImage ?: imgFeature.gone()
                    title ?: tvTitle.gone()
                }
            }
        }
    }
}