package com.carlosblaya.theagilemonkeystest.data.network.services

import com.carlosblaya.theagilemonkeystest.data.response.AlbumListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumsApiInterface {

    @GET("lookup")
    suspend fun getArtistAlbums(
        @Query("id") id: Long, @Query("entity") entity:String): AlbumListResponse

}