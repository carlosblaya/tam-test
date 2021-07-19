package com.carlosblaya.theagilemonkeystest.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArtistItemResponse(
    @Expose @SerializedName("artistId") val artistId: String,
    @Expose @SerializedName("artistName") val artistName: String,
    @Expose @SerializedName("primaryGenreName") val primaryGenreName: String?
)