package com.carlosblaya.theagilemonkeystest.data.pagingsources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.carlosblaya.theagilemonkeystest.data.response.mapper.ArtistListMapper
import com.carlosblaya.theagilemonkeystest.data.network.services.ArtistsApiInterface
import com.carlosblaya.theagilemonkeystest.domain.model.Artist
import com.carlosblaya.theagilemonkeystest.util.Konsts

class ArtistsPagingSource(
    private val service: ArtistsApiInterface,
    private val mapper: ArtistListMapper,
    private val term: String,
) : PagingSource<Int, Artist>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        val position = params.key ?: Konsts.INITIAL_LOAD_SIZE
        val offset = if (params.key != null) (position * Konsts.NETWORK_PAGE_SIZE) else Konsts.INITIAL_LOAD_SIZE
        Log.d("OFFSET", offset.toString())
        return try {
            val jsonResponse = service.getArtistList(term, Konsts.ENTITY_TYPE_MUSIC_ARTIST,offset,Konsts.LIMIT).data
            val response = mapper.toArtistList(jsonResponse)
            val nextKey = if (response.isEmpty()) {
                null
            } else {
                position + 1
            }
            Log.d("OFFSET KEY", nextKey.toString())
            LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward.
                // assume that if a full page is not loaded, that means the end of the data
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return null
    }
}