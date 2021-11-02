package com.revelatestudio.revelate.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.revelatestudio.revelate.data.source.remote.ArticleItem

@Database(
    entities = [News::class],
    version = 1
)
abstract class NewsDatabase : RoomDatabase(){
    abstract fun getNewsDao() : NewsDao

}