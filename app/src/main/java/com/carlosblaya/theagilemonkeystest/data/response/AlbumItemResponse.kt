package com.carlosblaya.theagilemonkeystest.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AlbumItemResponse(
    @Expose @SerializedName("collectionId") val collectionId: Long,
    @Expose @SerializedName("wrapperType") val wrapperType: String,
    @Expose @SerializedName("collectionName") val collectionName: String?,
    @Expose @SerializedName("artworkUrl100") val artworkUrl100: String?,
    @Expose @SerializedName("releaseDate") val releaseDate: String?
)