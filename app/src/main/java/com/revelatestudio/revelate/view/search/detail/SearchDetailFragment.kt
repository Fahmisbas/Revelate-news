package com.revelatestudio.revelate.view.search.detail

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.data.dataholder.SearchHistory
import com.revelatestudio.revelate.data.source.remote.NewsItemResponse
import com.revelatestudio.revelate.databinding.FragmentSearchDetailBinding
import com.revelatestudio.revelate.util.*
import com.revelatestudio.revelate.util.ext.gone
import com.revelatestudio.revelate.util.ext.onTextChanged
import com.revelatestudio.revelate.util.ext.visible
import com.revelatestudio.revelate.view.adapter.NewsListAdapter
import com.revelatestudio.revelate.view.search.SearchHistoryListAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchDetailFragment : Fragment() {

    private var _binding: FragmentSearchDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleSoftInput()

        with(binding) {
            newsListAdapter() { adapter ->
                rvGeneralNews.adapter = adapter

                editSearch.setOnKeyListener { _, keyCode, _ ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        val keyword = editSearch.text.toString()
                        saveKeyword(keyword)

                        observeNewsList(keyword, adapter)

                        hideSoftInputFromWindow()
                    }
                    true
                }
                observeIsNewsAlreadySaved(adapter)
            }

            searchHistoryAdapter() { adapter ->
                rvSearchHistory.adapter = adapter
                observeSearchHistories(adapter)
                editSearch.onTextChanged {
                    if (editSearch.text.toString().isEmpty()) {
                        observeSearchHistories(adapter)
                        searchHistory.visible()
                        btnClear.visible()
                    } else {
                        btnClear.visible()
                        searchHistory.gone()
                    }
                }

                btnClear.setOnClickListener {
                    viewModel.deleteSearchHistories()
                    observeSearchHistories(adapter)
                }
            }

            btnCancel.setOnClickListener {
                NavHostFragment.findNavController(this@SearchDetailFragment).navigateUp()
                hideSoftInputFromWindow()
            }


        }
    }

    private fun observeSearchHistories(adapter: SearchHistoryListAdapter) {
        viewModel.getSearchHistories().observe(viewLifecycleOwner, { searchHistories ->
            when(searchHistories) {
                is Resource.Success -> {
                    if (searchHistories.data?.isNotEmpty() == true) {
                        adapter.submitList(searchHistories.data)
                    }
                }
                is Resource.Empty -> {
                    adapter.submitList(null)
                    adapter.notifyDataSetChanged()
                    binding.titleSearchHistory.gone()
                    binding.btnClear.gone()
                }
                else -> {

                }
            }
        })
    }

    private fun saveKeyword(keyword: String) {
        viewModel.insertSearchHistory(SearchHistory(keyword))
    }

    private fun observeNewsList(keyword: String, adapter: NewsListAdapter) {
        with(binding) {
            viewModel.getNewsWithUserKeyword(keyword).observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        val news = response.data?.newsResponses
                        if (news != null) {
                            displayNews(news, adapter)
                            tvResult.text =
                                requireContext().resources.getString(R.string.result)
                        }
                    }
                    else -> {
                        val message = response.message
                        if (message != null) {
                            tvResult.text = message
                        } else tvResult.text = SERVER_ERR_MSG
                    }
                }
            })
        }

    }

    private fun observeIsNewsAlreadySaved(adapter: NewsListAdapter) {
        adapter.setOnGetArticlesItem(object : NewsListAdapter.OnGetNewsItem {
            override fun onArticlesItem(newsItemResponse: NewsItemResponse?) {
                if (newsItemResponse?.title != null) {
                    val newsLiveData = viewModel.getItemNews(newsItemResponse.title)
                    adapter.newsToObserve(newsLiveData, viewLifecycleOwner)
                }
            }
        })
    }

    private fun searchHistoryAdapter(adapterObj: (SearchHistoryListAdapter) -> Unit) {
        val searchHistoryAdapter = SearchHistoryListAdapter { searchHistory ->
            binding.editSearch.setText(searchHistory.keyword)
        }

        adapterObj.invoke(searchHistoryAdapter)

    }

    private fun newsListAdapter(adapterObj: (NewsListAdapter) -> Unit) {
        val newsAdapter = NewsListAdapter(onItemClick = {

        }, onSaveButtonClick = { toggleSave, articlesItem ->
            save(toggleSave, articlesItem)
        })

        adapterObj.invoke(newsAdapter)

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


    private fun toggleSoftInput() {
        val inputMethodManager: InputMethodManager? =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun hideSoftInputFromWindow() {
        val inputMethodManager: InputMethodManager? =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(binding.editSearch.windowToken, 0)
    }


    private fun displayNews(result: List<NewsItemResponse?>?, newsAdapter: NewsListAdapter) {
        if (result != null) {
            newsAdapter.submitList(result)
            binding.searchHistory.gone()
            binding.tvResult.visible()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        hideSoftInputFromWindow()
        _binding = null
    }

    companion object {
        fun newInstance() = SearchDetailFragment()
    }
}