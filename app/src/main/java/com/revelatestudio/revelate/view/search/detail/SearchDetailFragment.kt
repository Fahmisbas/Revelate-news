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
import com.revelatestudio.revelate.data.dataholder.SearchHistory
import com.revelatestudio.revelate.data.source.remote.ArticleItem
import com.revelatestudio.revelate.databinding.FragmentSearchDetailBinding
import com.revelatestudio.revelate.util.*
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

            val newsAdapter = NewsListAdapter(onItemClick = {
                onItemClick(it)
            }, onSaveButtonClick = { isSave, news ->

            },
            lifecycleOwner = viewLifecycleOwner)
            val searchHistoryAdapter = SearchHistoryListAdapter {
                onItemClick(it)
            }

            rvGeneralNews.adapter = newsAdapter
            rvSearchHistory.adapter = searchHistoryAdapter

            btnCancel.setOnClickListener {
                NavHostFragment.findNavController(this@SearchDetailFragment).navigateUp()
                hideSoftInputFromWindow()
            }

            editSearch.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    val keyword = editSearch.text.toString()
                    viewModel.getNewsWithUserKeyword(keyword)
                        .observe(viewLifecycleOwner, { response ->
                            val result = response.data?.articles
                            submitList(result, newsAdapter)
                        })
                    hideSoftInputFromWindow()
                }
                true
            }

            editSearch.onTextChanged {
                if (binding.editSearch.text.toString().isEmpty()) {
                    binding.searchHistory.visible()
                } else binding.searchHistory.gone()
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


    private fun submitList(result: List<ArticleItem?>?, newsAdapter: NewsListAdapter) {
        if (result != null) {
            newsAdapter.submitList(result)
            binding.searchHistory.gone()
            binding.tvResult.visible()
        }
    }

    private fun onItemClick(item: ArticleItem) {

    }

    private fun onItemClick(item: SearchHistory) {

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