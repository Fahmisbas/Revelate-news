package com.revelatestudio.revelate.view.headline.category.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.util.getNewsCategories
import com.revelatestudio.revelate.view.headline.category.CategoryFragment



class ViewPagerAdapter(val context : Context,fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val newsCategories = context.getNewsCategories()

    override fun getItemCount(): Int {
        return  newsCategories.size
    }

    override fun createFragment(position: Int): Fragment {
        return CategoryFragment.newInstance(newsCategories[position])
    }
}
