package com.revelatestudio.revelate.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.revelatestudio.revelate.data.source.remote.ArticleItem
import com.revelatestudio.revelate.databinding.FragmentSearchBinding
import com.revelatestudio.revelate.util.gone

import com.revelatestudio.revelate.view.adapter.NewsListAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NewsListAdapter {
            onItemClick(it)
        }

        binding.rvGeneralNews.adapter = adapter
        viewModel.getTopHeadlinesByCountry().observe(viewLifecycleOwner, { response ->
            val result = response.data?.articles
            if(result != null) {
                binding.loadingShimmer.root.gone()
                adapter.submitList(result)
            }

        })
        binding.search.editSearch.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSearchDetailFragment()
            findNavController().navigate(action)
        }
    }

    private fun onItemClick(item: ArticleItem) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}