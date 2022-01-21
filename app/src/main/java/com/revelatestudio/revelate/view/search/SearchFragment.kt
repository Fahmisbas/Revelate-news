package com.revelatestudio.revelate.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.data.source.remote.NewsItemResponse
import com.revelatestudio.revelate.databinding.FragmentSearchBinding
import com.revelatestudio.revelate.util.ext.gone
import com.revelatestudio.revelate.util.ext.visible

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


        newsListAdapter() { adapter ->
            binding.rvGeneralNews.adapter = adapter

            displayListArticles(adapter)

            observeIsNewsAlreadySaved(adapter)

        }



        binding.search.editSearch.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToSearchDetailFragment()
            findNavController().navigate(action)
        }
    }

    private fun displayListArticles(adapter: NewsListAdapter) {
        viewModel.getTopHeadlinesByCountry().observe(viewLifecycleOwner, { response ->
            val result = response.data?.newsResponses
            if(result != null) {
                binding.loadingShimmer.root.gone()
                binding.titleGeneralNews.visible()

                adapter.submitList(result)
            }
        })
    }

    private fun observeIsNewsAlreadySaved(adapter: NewsListAdapter) {
        adapter.setOnGetArticlesItem(object : NewsListAdapter.OnGetNewsItem{
            override fun onArticlesItem(newsItemResponse: NewsItemResponse?) {
                if (newsItemResponse?.title != null) {
                    val newsLiveData = viewModel.getItemNews(newsItemResponse.title)
                    adapter.newsToObserve(newsLiveData, viewLifecycleOwner)
                }
            }
        })
    }

    private fun newsListAdapter(adapterObj: (NewsListAdapter) -> Unit) {
        val adapter = NewsListAdapter(onItemClick = {

        }, onSaveButtonClick = { toggleSave, articlesItem ->
            save(toggleSave, articlesItem)
        })
        adapterObj.invoke(adapter)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}