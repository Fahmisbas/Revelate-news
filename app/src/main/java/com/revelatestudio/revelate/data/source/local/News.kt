package com.revelatestudio.revelate.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey



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

) {
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null
}
