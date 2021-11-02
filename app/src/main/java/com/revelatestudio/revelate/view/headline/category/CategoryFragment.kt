package com.revelatestudio.revelate.view.headline.category

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.data.dataholder.NewsCategory
import com.revelatestudio.revelate.data.source.local.News
import com.revelatestudio.revelate.data.source.remote.ArticleItem
import com.revelatestudio.revelate.databinding.FragmentCategoryBinding
import com.revelatestudio.revelate.databinding.LayoutNoInternetConnectionBinding
import com.revelatestudio.revelate.util.*
import com.revelatestudio.revelate.util.Locale.US
import com.revelatestudio.revelate.view.adapter.NewsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val NEWS_CATEGORY = "news_category"

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private lateinit var newsCategory: NewsCategory

    private var _mainBinding: FragmentCategoryBinding? = null
    private val binding get() = _mainBinding!!

    private var _noInternetConnectionBinding: LayoutNoInternetConnectionBinding? = null
    private val noInternetConnectionBinding get() = _noInternetConnectionBinding!!

    private val viewModel: CategoryViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            val newsCategory = bundle.getParcelable<NewsCategory>(NEWS_CATEGORY)
            if (newsCategory != null) {
                this.newsCategory = newsCategory
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val isPhoneConnected: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireContext().getPhoneConnectionStatus()
        } else {
            requireContext().getPhoneConnectionStatusOldApi() ?: false
        }
        _noInternetConnectionBinding =
            LayoutNoInternetConnectionBinding.inflate(inflater, container, false)
        _mainBinding = FragmentCategoryBinding.inflate(inflater, container, false)
        return if (isPhoneConnected) binding.root else noInternetConnectionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            lifecycleScope.launch {
                viewModel.setDefaultCountryCode(requireContext().getPreferenceCountry() ?: US)
            }

            newsListAdapter() { adapter ->
                rvHeadlinesCategory.adapter = adapter

                observeIsDataAlreadySaved(adapter)

                displayArticles(adapter) {

                }
                swipeRefresh.setOnRefreshListener {
                    displayArticles(adapter) {

                    }
                }

            }
        }
    }

    private fun newsListAdapter(adapterObj: (NewsListAdapter) -> Unit) {
        val adapter = NewsListAdapter(onItemClick = {

        }, onSaveButtonClick = { toggleSave, articlesItem ->
            save(toggleSave, articlesItem)
        })
        adapterObj.invoke(adapter)
    }

    private fun observeIsDataAlreadySaved(adapter: NewsListAdapter) {
        adapter.setOnGetArticlesItem(object : NewsListAdapter.OnGetArticlesItem{
            override fun onArticlesItem(articleItem: ArticleItem?) {
                if (articleItem?.title != null) {
                    val newsLiveData = viewModel.getItemNews(articleItem.title)
                    adapter.newsToObserve(newsLiveData, viewLifecycleOwner)
                }
            }
        })
    }


    private var saveNewsId: Long = 0
    private fun save(toggleSave: Boolean, articleItem: ArticleItem) {
        with(articleItem) {
            val news = News(
                publishedAt = publishedAt,
                author = author,
                urlToImage = urlToImage,
                description = description,
                source = source?.name,
                title = title,
                url = url,
                content = content,
            )
            saveNewsId = id ?: -1
            if (toggleSave) {
                viewModel.insertNews(news).observe(viewLifecycleOwner, { id ->
                    this@CategoryFragment.saveNewsId = id
                })
            } else {
                news.id = saveNewsId
                viewModel.deleteNews(news = news)
            }
        }
    }


    private fun displayArticles(
        adapter: NewsListAdapter,
        onSuccess: () -> Unit
    ) {
        with(binding) {
            viewModel.getTopHeadlinesByCountryWithCategory(newsCategory.categoryName)
                .observe(viewLifecycleOwner, { response ->
                    when (response) {
                        is Resource.Success -> {
                            onSuccess.invoke()
                            val articles = response.data?.articles
                            if (articles != null) {
                                adapter.submitList(articles)
                                tvHeadline.text =
                                    requireContext().resources.getString(R.string.top_headlines)
                                loadingUiFinish()
                            }
                        }
                        else -> {
                            val message = response.message
                            if (message != null) {
                                tvHeadline.text = message
                                loadingUiFinish()
                            } else tvHeadline.text = ERR_MSG
                        }
                    }
                })
        }
    }

    private fun loadingUiFinish() {
        with(binding) {
            loadingShimmer.root.gone()
            swipeRefresh.isRefreshing = false
            tvHeadline.visible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mainBinding = null
        _noInternetConnectionBinding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(newsCategory: NewsCategory) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(NEWS_CATEGORY, newsCategory)
                }
            }
    }
}