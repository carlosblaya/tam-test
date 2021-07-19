package com.carlosblaya.theagilemonkeystest.data.response.mapper

import com.carlosblaya.theagilemonkeystest.data.database.entities.Likes
import com.carlosblaya.theagilemonkeystest.data.response.SongItemResponse
import com.carlosblaya.theagilemonkeystest.domain.model.Song

class SongListMapper {

    fun toSongList(json: List<SongItemResponse>?): List<Song> {
        with(json) {
            return if (this?.isNotEmpty() == true) {
                this.map { toSong(it) }
            } else {
                emptyList()
            }
        }
    }

    private fun toSong(json: SongItemResponse): Song {
        with(json) {
            return Song(
                collectionId = collectionId,
                wrapperType = wrapperType,
                kind = kind,
                trackId = trackId,
                artistName = artistName,
                artworkUrl100 = artworkUrl100,
                trackName = trackName,
                previewUrl = previewUrl,
                isLiked = false
            )
        }
    }

    fun toLikeItemEntity(song: Song): Likes {
        with(song) {
            return Likes(
                trackId = trackId,
                trackName = trackName
            )
        }
    }
}