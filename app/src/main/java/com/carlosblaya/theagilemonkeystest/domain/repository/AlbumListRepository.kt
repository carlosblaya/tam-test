package com.carlosblaya.theagilemonkeystest.domain.repository

import com.carlosblaya.theagilemonkeystest.data.response.AlbumListResponse
import com.carlosblaya.theagilemonkeystest.data.network.ResponseHandler
import com.carlosblaya.theagilemonkeystest.data.network.services.AlbumsApiInterface
import com.carlosblaya.theagilemonkeystest.util.Konsts
import com.carlosblaya.theagilemonkeystest.util.Resource

class AlbumListRepository(
    private val albumsApiInterface: AlbumsApiInterface,
    private val responseHandler: ResponseHandler
){
    suspend fun getArtistAlbums(idArtist:Long): Resource<AlbumListResponse> {
        return try {
            val response = albumsApiInterface.getArtistAlbums(idArtist, Konsts.ENTITY_ALBUM)
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}