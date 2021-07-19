package com.carlosblaya.theagilemonkeystest.domain.model

data class Album(
    val collectionId: Long,
    val wrapperType:String,
    val collectionName: String?,
    val artworkUrl100: String?,
    val releaseDate: String?,
){
    companion object {
        val KEY_COLLECTION_ID = "collection_id"
        val KEY_COLLECTION_NAME = "collection_name"
    }
}