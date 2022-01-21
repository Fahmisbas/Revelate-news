package com.revelatestudio.revelate.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.revelatestudio.revelate.data.dataholder.News
import com.revelatestudio.revelate.data.dataholder.SearchHistory

@Database(
    entities = [News::class, SearchHistory::class],
    version = 1
)
abstract class NewsDatabase : RoomDatabase(){
    abstract fun getNewsDao() : NewsDao
}