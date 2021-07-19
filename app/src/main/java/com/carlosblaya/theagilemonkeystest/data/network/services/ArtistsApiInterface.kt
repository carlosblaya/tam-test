package com.carlosblaya.theagilemonkeystest.data.network.services

import com.carlosblaya.theagilemonkeystest.data.response.ArtistListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ArtistsApiInterface {

    @GET("search")
    suspend fun getArtistList(
        @Query("term") term: String, @Query("entity") entity:String, @Query("offset") offset:Int, @Query("limit") limit:Int): ArtistListResponse

}