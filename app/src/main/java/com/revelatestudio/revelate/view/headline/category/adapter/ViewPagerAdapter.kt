package com.revelatestudio.revelate.view.headline.category.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.revelatestudio.revelate.model.dataholder.Category


class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(categoryPosition: Int): Fragment {
        return CategoryFragment.newInstance(Category.newsCategories[categoryPosition].position)
    }
}
