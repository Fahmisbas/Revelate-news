package com.revelatestudio.revelate.view.headline.category

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.revelatestudio.revelate.data.dataholder.NewsCategory
import com.revelatestudio.revelate.data.source.remote.NewsResponse
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
            val data = bundle.getParcelable<NewsCategory>(NEWS_CATEGORY)
            if (data != null) {
                newsCategory = data
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

        lifecycleScope.launch {
            viewModel.setDefaultCountryCode(requireContext().getPreferenceCountry() ?: US)
        }

        val adapter = NewsListAdapter(onItemClick = {})
        binding.rvHeadlinesCategory.adapter = adapter

        viewModel.getTopHeadlinesByCountryWithCategory(newsCategory.categoryName)
            .observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        val data = response.data
                        if (data != null) {
                            binding.loadingShimmer.root.gone()
                            displayArticles(adapter, data)
                        }
                    }
                    else -> {
                        val message = response.message
                        if (message != null) {
                            binding.loadingShimmer.root.gone()
                            binding.tvHeadline.visible()
                            binding.tvHeadline.text = message
                        }
                    }
                }
            })
    }


    private fun displayArticles(
        adapter: NewsListAdapter,
        response: NewsResponse
    ) {
        val result = response.articles
        if (result != null) {
            adapter.submitList(result)
            binding.tvHeadline.visible()
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