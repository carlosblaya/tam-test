package com.carlosblaya.theagilemonkeystest.domain.repository

import com.carlosblaya.theagilemonkeystest.data.response.SongListResponse
import com.carlosblaya.theagilemonkeystest.data.network.ResponseHandler
import com.carlosblaya.theagilemonkeystest.data.network.services.SongsApiInterface
import com.carlosblaya.theagilemonkeystest.util.Konsts
import com.carlosblaya.theagilemonkeystest.util.Resource

class SongListRepository(
    private val songsApiInterface: SongsApiInterface,
    private val responseHandler: ResponseHandler
){
    suspend fun getAlbumSongs(idAlbum:Long): Resource<SongListResponse> {
        return try {
            val response = songsApiInterface.getAlbumSongs(idAlbum, Konsts.ENTITY_SONG)
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}