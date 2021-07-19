package com.carlosblaya.theagilemonkeystest.data.response.mapper

import com.carlosblaya.theagilemonkeystest.data.response.ArtistItemResponse
import com.carlosblaya.theagilemonkeystest.domain.model.Artist

class ArtistListMapper {

    fun toArtistList(json: List<ArtistItemResponse>?): List<Artist> {
        with(json) {
            return if (this?.isNotEmpty() == true) {
                this.map { toArtist(it) }
            } else {
                emptyList()
            }
        }
    }

    private fun toArtist(json: ArtistItemResponse): Artist {
        with(json) {
            return Artist(
                artistId = artistId,
                artistName = artistName,
                primaryGenreName = primaryGenreName
            )
        }
    }
}