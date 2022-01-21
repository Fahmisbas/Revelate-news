package com.revelatestudio.revelate.view.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.databinding.FragmentSavedArticleBinding
import com.revelatestudio.revelate.util.ext.gone
import com.revelatestudio.revelate.util.ext.visible
import com.revelatestudio.revelate.view.adapter.SavedNewsListAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SavedArticleFragment : Fragment() {

    private var _binding : FragmentSavedArticleBinding? = null
    private val binding get() =  _binding!!

    private val viewModel : SavedArticleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            newsListAdapter() { adapter ->
                rvSavedNews.adapter = adapter
                observeNewsList(adapter)
            }
        }

    }

    private fun observeNewsList(adapter: SavedNewsListAdapter) {
        viewModel.getSavedNews().observe(viewLifecycleOwner, { news ->
            displayNews(news, adapter)
        })
    }

    private fun displayNews(news: List<News>?, adapter: SavedNewsListAdapter) {
        if (!news.isNullOrEmpty()) {
            binding.tvEmpty.gone()
            adapter.submitList(news)
        } else {
            binding.tvEmpty.visible()
        }
    }

    private fun newsListAdapter(adapterObj: (SavedNewsListAdapter) -> Unit) {
        val adapter = SavedNewsListAdapter(onItemClick = {

        }, onSaveButtonClick = { toggle, news ->
            if (toggle) {
                viewModel.insertNews(news)
            } else {
                viewModel.deleteNews(news)
            }
        })
        adapterObj.invoke(adapter)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SavedArticleFragment()
    }
}