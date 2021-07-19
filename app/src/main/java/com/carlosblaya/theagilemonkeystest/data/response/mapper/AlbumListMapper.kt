package com.carlosblaya.theagilemonkeystest.data.response.mapper

import com.carlosblaya.theagilemonkeystest.data.response.AlbumItemResponse
import com.carlosblaya.theagilemonkeystest.domain.model.Album

class AlbumListMapper {

    fun toAlbumList(json: List<AlbumItemResponse>?): List<Album> {
        with(json) {
            return if (this?.isNotEmpty() == true) {
                this.map { toAlbum(it) }
            } else {
                emptyList()
            }
        }
    }

    private fun toAlbum(json: AlbumItemResponse): Album {
        with(json) {
            return Album(
                collectionId = collectionId,
                wrapperType = wrapperType,
                collectionName = collectionName,
                artworkUrl100 = artworkUrl100,
                releaseDate = releaseDate
            )
        }
    }
}