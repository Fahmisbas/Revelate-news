package com.revelatestudio.revelate.view.headline.category

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.data.dataholder.NewsCategory
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.data.source.remote.NewsItemResponse
import com.revelatestudio.revelate.data.source.remote.NewsResponse
import com.revelatestudio.revelate.databinding.FragmentCategoryBinding
import com.revelatestudio.revelate.databinding.LayoutNoInternetConnectionBinding
import com.revelatestudio.revelate.util.*
import com.revelatestudio.revelate.util.Locale.US
import com.revelatestudio.revelate.util.ext.getPreferenceCountry
import com.revelatestudio.revelate.util.ext.gone
import com.revelatestudio.revelate.util.ext.visible
import com.revelatestudio.revelate.view.adapter.NewsListAdapter
import com.revelatestudio.revelate.view.detail.DetailActivity
import com.revelatestudio.revelate.view.detail.EXTRA_NEWS
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

            newsListAdapter { adapter ->
                rvHeadlinesCategory.adapter = adapter

                observeIsNewsItemAlreadySaved(adapter)

                observeNewsList(adapter)

                swipeRefresh.setOnRefreshListener {
                    observeNewsList(adapter)
                }

            }
        }
    }

    private fun newsListAdapter(adapterObj: (NewsListAdapter) -> Unit) {
        val adapter = NewsListAdapter(onItemClick = { news ->
            navigateToDetailFragment(news)
        }, onSaveButtonClick = { toggleSave, articlesItem ->
            save(toggleSave, articlesItem)
        })
        adapterObj.invoke(adapter)
    }

    private fun navigateToDetailFragment(newsResponse: NewsItemResponse) {

        newsResponse.apply {
            val news = News(
                publishedAt = publishedAt,
                author = author,
                urlToImage = urlToImage,
                description = description,
                source = source?.name,
                title = title,
                url = url,
                content = content
            )

            activity?.let{
                val intent = Intent (it, DetailActivity::class.java)
                intent.putExtra(EXTRA_NEWS, news)
                it.startActivity(intent)
            }

        }

    }

    private fun observeIsNewsItemAlreadySaved(adapter: NewsListAdapter) {
        adapter.setOnGetArticlesItem(object : NewsListAdapter.OnGetNewsItem {
            override fun onArticlesItem(newsItemResponse: NewsItemResponse?) {
                if (newsItemResponse?.title != null) {
                    val newsLiveData = viewModel.getItemNews(newsItemResponse.title)
                    adapter.newsToObserve(newsLiveData, viewLifecycleOwner)
                }
            }
        })
    }


    private fun save(toggleSave: Boolean, newsItemResponse: NewsItemResponse) {
        var saveNewsId: Long
        with(newsItemResponse) {
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
                    saveNewsId = id
                })
            } else {
                news.id = saveNewsId
                viewModel.deleteNews(news)
            }
        }
    }


    private fun observeNewsList(
        adapter: NewsListAdapter,
    ) {

        viewModel.getTopHeadlinesByCountryWithCategory(newsCategory.categoryName)
            .observe(viewLifecycleOwner, { response ->
                displayNews(response, adapter)
            })

    }

    private fun displayNews(response: Resource<NewsResponse>?, adapter: NewsListAdapter) {
        with(binding) {
            when (response) {
                is Resource.Success -> {
                    val news = response.data?.newsResponses
                    if (news != null) {
                        adapter.submitList(news)
                        tvHeadline.text =
                            requireContext().resources.getString(R.string.top_headlines)
                        loadingUiFinish()
                    }
                }
                else -> {
                    val message = response?.message
                    if (message != null) {
                        tvHeadline.text = message
                        loadingUiFinish()
                    } else tvHeadline.text = SERVER_ERR_MSG
                }
            }
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