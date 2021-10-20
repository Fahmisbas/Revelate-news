package com.revelatestudio.revelate.view.headline.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.databinding.FragmentCategoryBinding
import com.revelatestudio.revelate.util.*


private const val NEWS_CATEGORY = "news_category"

class CategoryFragment : Fragment() {

    private var newsCategory: Int? = null
    private var _binding : FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newsCategory = it.getInt(NEWS_CATEGORY)
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

        when(newsCategory) {
            BUSINESS -> {

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
        fun newInstance(newsCategory: Int) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(NEWS_CATEGORY, newsCategory)
                }
            }
    }
}