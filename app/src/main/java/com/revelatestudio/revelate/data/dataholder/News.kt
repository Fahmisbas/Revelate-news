package com.revelatestudio.revelate.data.dataholder

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "news_table")
data class News(

    val publishedAt: String? = null,
    val author: String? = null,
    val urlToImage: String? = null,
    val description: String? = null,
    val source: String? = null,
    val title: String? = null,
    val url: String? = null,
    val content: String? = null,

) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null
}
