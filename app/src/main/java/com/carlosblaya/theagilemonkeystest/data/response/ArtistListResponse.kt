package com.carlosblaya.theagilemonkeystest.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArtistListResponse(
    @Expose
    @SerializedName("results")
    val data: MutableList<ArtistItemResponse>?

)