package com.carlosblaya.theagilemonkeystest.domain.model

data class Song(
    val collectionId: Long,
    val wrapperType: String?,
    val kind:String?,
    val trackId:Long,
    val artistName: String?,
    val artworkUrl100: String?,
    val trackName: String?,
    var previewUrl: String?,
    var isLiked: Boolean
){
    companion object {
        val KEY_PREVIEW_URL = "preview_url"
        val KIND_TYPE_MUSIC_VIDEO = "music-video"
    }
}
