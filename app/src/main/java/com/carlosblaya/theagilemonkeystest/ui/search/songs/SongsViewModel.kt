package com.carlosblaya.theagilemonkeystest.ui.search.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.carlosblaya.theagilemonkeystest.data.database.AppDatabase
import com.carlosblaya.theagilemonkeystest.data.response.SongListResponse
import com.carlosblaya.theagilemonkeystest.data.response.mapper.SongListMapper
import com.carlosblaya.theagilemonkeystest.ui.base.BaseViewModel
import com.carlosblaya.theagilemonkeystest.domain.model.Song
import com.carlosblaya.theagilemonkeystest.domain.repository.SongListRepository
import com.carlosblaya.theagilemonkeystest.util.Resource
import kotlinx.coroutines.Dispatchers

class SongsViewModel(
    private val songsRepository: SongListRepository,
    private val database:AppDatabase,
) : BaseViewModel() {

    var songListMapper: SongListMapper = SongListMapper()

    fun getAlbumSongs(idAlbum:Long): LiveData<Resource<SongListResponse>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        emit(songsRepository.getAlbumSongs(idAlbum))
    }

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