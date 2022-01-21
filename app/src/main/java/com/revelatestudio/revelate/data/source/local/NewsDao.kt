package com.revelatestudio.revelate.data.source.local

import androidx.room.*
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.data.dataholder.SearchHistory


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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(searchHistory: SearchHistory) : Long

    @Query("SELECT * FROM search_history_table")
    suspend fun getSearchHistories() : List<SearchHistory>

    @Query("DELETE FROM search_history_table")
    suspend fun deleteSearchHistories()


}