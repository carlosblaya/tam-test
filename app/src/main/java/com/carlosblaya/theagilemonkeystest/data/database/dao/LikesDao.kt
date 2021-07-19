package com.carlosblaya.theagilemonkeystest.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.carlosblaya.theagilemonkeystest.data.database.entities.Likes

@Dao
interface LikesDao : BaseDao<Likes> {

    @Query("SELECT * FROM likes WHERE trackId = :id")
    fun exists(id: Long): Boolean

}