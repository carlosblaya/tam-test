package com.carlosblaya.theagilemonkeystest.domain.model

data class Artist(
    val artistId: String,
    val artistName: String,
    val primaryGenreName: String?,
){
    companion object {
        val KEY_ARTIST_ID = "artist_id"
        val KEY_ARTIST_NAME = "artist_name"
    }
}