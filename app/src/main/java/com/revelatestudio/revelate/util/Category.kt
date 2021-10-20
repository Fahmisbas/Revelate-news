package com.revelatestudio.revelate.util

import com.revelatestudio.revelate.model.dataholder.NewsCategory

object Category {
    val newsCategories = arrayListOf(
        NewsCategory("Business", BUSINESS),
        NewsCategory("Entertainment", ENTERTAINMENT),
        NewsCategory("General", GENERAL),
        NewsCategory("Health", HEALTH),
        NewsCategory("Science", SCIENCE),
        NewsCategory("Technology", TECHNOLOGY)
    )

}