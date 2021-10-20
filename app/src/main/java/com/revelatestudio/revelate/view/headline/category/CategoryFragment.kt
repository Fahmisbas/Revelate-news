package com.revelatestudio.revelate.view.headline.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.revelatestudio.revelate.databinding.FragmentCategoryBinding
import com.revelatestudio.revelate.data.dataholder.NewsCategory

import com.revelatestudio.revelate.util.*
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

        viewModel.setDefaultCountryCode("us")

        when (newsCategory?.position) {
            BUSINESS -> {
                viewModel.headlinesWithCategory.observe(viewLifecycleOwner, Observer {
//                    viewModel.getTopHeadlinesByCountryWithCategory(category =  )
                })
            }
            TECHNOLOGY -> {

            }
            ENTERTAINMENT -> {

            }
            GENERAL -> {

            }
            HEALTH -> {

            }
            SCIENCE -> {

            }
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