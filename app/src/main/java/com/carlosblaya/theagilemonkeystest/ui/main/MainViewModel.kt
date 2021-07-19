package com.carlosblaya.theagilemonkeystest.ui.main

import com.carlosblaya.theagilemonkeystest.data.database.AppDatabase
import com.carlosblaya.theagilemonkeystest.data.response.mapper.SongListMapper
import com.carlosblaya.theagilemonkeystest.ui.base.BaseViewModel
import com.carlosblaya.theagilemonkeystest.domain.model.Song

class MainViewModel(
    private val database:AppDatabase,
) : BaseViewModel() {

    var songListMapper: SongListMapper = SongListMapper()

    fun checkIfLikeExist(idSong:Long):Boolean{
        return database.likesDao.exists(idSong)
    }

    fun saveLike(song: Song) {
        database.likesDao.insert(songListMapper.toLikeItemEntity(song))
    }

    fun deleteLike(song: Song) {
        database.likesDao.delete(songListMapper.toLikeItemEntity(song))
    }

}