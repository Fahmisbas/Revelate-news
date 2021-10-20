package com.revelatestudio.revelate.data.dataholder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsCategory(
    val categoryName : String,
    val position : Int
) : Parcelable
