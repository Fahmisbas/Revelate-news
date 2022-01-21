package com.revelatestudio.revelate.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.databinding.ItemNewsBinding
import com.revelatestudio.revelate.util.ext.getProgressDrawable
import com.revelatestudio.revelate.util.getRelativeTimeSpanString
import com.revelatestudio.revelate.util.ext.gone
import com.revelatestudio.revelate.util.ext.loadImage


class SavedNewsListAdapter(
    private val onItemClick: (News) -> Unit,
    private val onSaveButtonClick: (Boolean, News) -> Unit
) : ListAdapter<News, SavedNewsListAdapter.SavedNewsViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedNewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedNewsViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: SavedNewsViewHolder, position: Int) {
        val newsItem = currentList[position]

        holder.bind(newsItem) { toggle, news ->
            onSaveButtonClick.invoke(toggle, news)
        }


    }

    inner class SavedNewsViewHolder(val binding : ItemNewsBinding, val context: Context)  : RecyclerView.ViewHolder(binding.root){
        var toggle = true
        fun bind(newsItem: News?, toggleDelete : (Boolean, News) -> Unit) {
            newsItem?.apply {

                with(binding) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmarked)

                    imgFeature.loadImage(urlToImage, getProgressDrawable(context))
                    tvTitle.text = title
                    tvBody.text = description
                    tvSource.text = "${author ?: "Revelate News"} - ${
                        getRelativeTimeSpanString(publishedAt)
                    }" ?: "Revelate News"

                    btnBookmark.setOnClickListener {
                        toggle = !toggle
                        if (toggle) {
                            btnBookmark.setImageResource(R.drawable.ic_bookmarked)
                        } else btnBookmark.setImageResource(R.drawable.ic_bookmark)

                        toggleDelete.invoke(toggle, newsItem)
                    }

                    description ?: tvBody.gone()
                    urlToImage ?: imgFeature.gone()
                    title ?: tvTitle.gone()

                }


            }
        }
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItemResponse: News, newItemResponse: News): Boolean {
                return oldItemResponse.id == newItemResponse.id
            }
            override fun areContentsTheSame(oldItemResponse: News, newItemResponse: News): Boolean {
                return oldItemResponse.content == newItemResponse.content
            }
        }
    }


}