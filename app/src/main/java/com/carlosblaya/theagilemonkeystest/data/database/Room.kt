package com.carlosblaya.theagilemonkeystest.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carlosblaya.theagilemonkeystest.data.database.dao.LikesDao
import com.carlosblaya.theagilemonkeystest.data.database.entities.Likes

@Database(
    entities = [
        Likes::class
    ], version = 1, exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract val likesDao: LikesDao
}

