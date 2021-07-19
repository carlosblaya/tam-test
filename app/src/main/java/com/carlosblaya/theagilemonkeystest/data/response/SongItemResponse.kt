package com.carlosblaya.theagilemonkeystest.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SongItemResponse(
    @Expose @SerializedName("collectionId") val collectionId: Long,
    @Expose @SerializedName("trackId") val trackId: Long,
    @Expose @SerializedName("wrapperType") val wrapperType: String?,
    @Expose @SerializedName("kind") val kind: String?,
    @Expose @SerializedName("artistName") val artistName: String?,
    @Expose @SerializedName("artworkUrl100") val artworkUrl100: String?,
    @Expose @SerializedName("trackName") val trackName: String?,
    @Expose @SerializedName("previewUrl") val previewUrl: String?
)