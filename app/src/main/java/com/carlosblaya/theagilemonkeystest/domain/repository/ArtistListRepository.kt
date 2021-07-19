package com.carlosblaya.theagilemonkeystest.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.carlosblaya.theagilemonkeystest.data.response.mapper.ArtistListMapper
import com.carlosblaya.theagilemonkeystest.data.network.services.ArtistsApiInterface
import com.carlosblaya.theagilemonkeystest.data.pagingsources.ArtistsPagingSource
import com.carlosblaya.theagilemonkeystest.domain.model.Artist
import com.carlosblaya.theagilemonkeystest.util.Konsts

interface ArtistListRepository {
    suspend fun getArtistList(term:String): LiveData<PagingData<Artist>>
}

class ArtistRepositoryImpl(
    private val artistService: ArtistsApiInterface,
    private val mapper: ArtistListMapper,
) : ArtistListRepository {
    override suspend fun getArtistList(term:String): LiveData<PagingData<Artist>> {
        return Pager(
            config = PagingConfig(
                pageSize = Konsts.NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                ArtistsPagingSource(artistService, mapper,term)
            }
        ).liveData
    }
}