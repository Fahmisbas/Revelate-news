package com.revelatestudio.revelate.view.headline

import android.os.Bundle
import android.view.*
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.databinding.FragmentHeadlineBinding
import com.revelatestudio.revelate.util.*
import com.revelatestudio.revelate.util.Locale.INDONESIA
import com.revelatestudio.revelate.util.Locale.US
import com.revelatestudio.revelate.util.ext.Preferences.COUNTRY_PREF_KEY
import com.revelatestudio.revelate.util.ext.dataStore
import com.revelatestudio.revelate.util.ext.getNewsCategories
import com.revelatestudio.revelate.view.MainActivity
import com.revelatestudio.revelate.view.headline.category.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HeadlineFragment : Fragment() {

    private var _binding: FragmentHeadlineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeadlineBinding.inflate(inflater, container, false)
        val rootActivity = (requireActivity() as MainActivity)
        with(rootActivity) {
            setSupportActionBar(binding.toolbar.root)
            setHasOptionsMenu(true);
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter =
            ViewPagerAdapter(requireContext(), requireActivity().supportFragmentManager, lifecycle)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tabTitle, position ->
            tabTitle.text = requireContext().getNewsCategories()[position].categoryName
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.indonesia -> {
                lifecycleScope.launch {
                    saveCountryPreference(INDONESIA)
                }
            }
            R.id.us -> {
                lifecycleScope.launch {
                    saveCountryPreference(US)
                }
            }
        }
        return true
    }

    private suspend fun saveCountryPreference(country: String) {
        val dataStoreKey = COUNTRY_PREF_KEY
        requireContext().dataStore.edit { countryPreference ->
            countryPreference[dataStoreKey] = country
        }
        requireActivity().recreateActivity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = HeadlineFragment()
    }
}