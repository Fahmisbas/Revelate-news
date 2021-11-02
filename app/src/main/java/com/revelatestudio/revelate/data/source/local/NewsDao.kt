package com.revelatestudio.revelate.data.source.local

import androidx.room.*


@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: News) : Long

    @Delete
    suspend fun deleteNews(news : News)

    @Query("SELECT * FROM news_table")
    suspend fun getAllNews() : List<News>

    @Query("SELECT * FROM news_table WHERE title = :title LIMIT 1")
    suspend fun getItemNews(title : String) : News?

}