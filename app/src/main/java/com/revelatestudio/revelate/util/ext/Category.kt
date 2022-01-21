package com.revelatestudio.revelate.util.ext

import android.content.Context
import com.revelatestudio.revelate.R
import com.revelatestudio.revelate.data.dataholder.NewsCategory


fun Context.getNewsCategories() : ArrayList<NewsCategory>{
    val stringArrayCategories = resources.getStringArray(R.array.categories)
    val categories = arrayListOf<NewsCategory>()
    for ((index, category) in stringArrayCategories.withIndex()) {
        categories.add(NewsCategory(category, index))
    }
    return categories
}