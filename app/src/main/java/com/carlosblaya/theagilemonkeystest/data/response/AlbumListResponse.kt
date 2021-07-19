package com.carlosblaya.theagilemonkeystest.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AlbumListResponse(
    @Expose
    @SerializedName("results")
    val albums: List<AlbumItemResponse>?

)