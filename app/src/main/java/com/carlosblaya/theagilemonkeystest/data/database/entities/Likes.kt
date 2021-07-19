package com.carlosblaya.theagilemonkeystest.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "likes")
data class Likes(
    @PrimaryKey
    var trackId: Long = 0,
    var trackName: String? = ""
)



