package com.revelatestudio.revelate.data.dataholder

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "search_history_table")
data class SearchHistory(
    @PrimaryKey
    val keyword : String
) {
}