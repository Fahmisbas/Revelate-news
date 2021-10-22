package com.revelatestudio.revelate.view.headline.category

import android.os.Bundle
import com.revelatestudio.revelate.util.gone
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.revelatestudio.revelate.databinding.FragmentCategoryBinding
import com.revelatestudio.revelate.data.dataholder.NewsCategory
import com.revelatestudio.revelate.data.source.remote.NewsResponse

import com.revelatestudio.revelate.util.*
import com.revelatestudio.revelate.view.adapter.NewsListAdapter
import dagger.hilt.android.AndroidEntryPoint


private const val NEWS_CATEGORY = "news_category"

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var newsCategory: NewsCategory? = null
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            newsCategory = bundle.getParcelable(NEWS_CATEGORY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NewsListAdapter { item ->

        }
        adapter.setContext(requireContext())
        binding.rvHeadlinesCategory.adapter = adapter

        viewModel.setDefaultCountryCode("us")
        newsCategory?.let { category ->
            viewModel.getTopHeadlinesByCountryWithCategory(category.categoryName).observe(viewLifecycleOwner, { response ->
                if (response.data != null) {
                    binding.loadingShimmer.root.gone()
                    displayListArticlesItem(adapter, response)
                }
            })
        }
    }

    private fun displayListArticlesItem(adapter: NewsListAdapter, response: Resource<NewsResponse>?) {
        val result = response?.data?.articles
        if (result != null) {
            adapter.submitList(result)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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