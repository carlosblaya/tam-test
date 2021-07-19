package com.carlosblaya.theagilemonkeystest.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SongListResponse(
    @Expose
    @SerializedName("results")
    val songs: List<SongItemResponse>?

)