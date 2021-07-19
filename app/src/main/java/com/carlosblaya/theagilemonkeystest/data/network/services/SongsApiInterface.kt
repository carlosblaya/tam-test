package com.carlosblaya.theagilemonkeystest.data.network.services

import com.carlosblaya.theagilemonkeystest.data.response.SongListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SongsApiInterface {

    @GET("lookup")
    suspend fun getAlbumSongs(
        @Query("id") id: Long, @Query("entity") entity:String): SongListResponse

}