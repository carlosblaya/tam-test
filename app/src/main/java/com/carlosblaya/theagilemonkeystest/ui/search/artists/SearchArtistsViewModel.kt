package com.carlosblaya.theagilemonkeystest.ui.search.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.carlosblaya.theagilemonkeystest.ui.base.BaseViewModel
import com.carlosblaya.theagilemonkeystest.domain.model.Artist
import com.carlosblaya.theagilemonkeystest.domain.repository.ArtistListRepository

class SearchArtistsViewModel(
    private val artistRepository: ArtistListRepository
) : BaseViewModel() {

    private val _artistList = MutableLiveData<PagingData<Artist>>()

    suspend fun getArtistList(term:String): LiveData<PagingData<Artist>> {
        val response = artistRepository.getArtistList(term).cachedIn(viewModelScope)
        _artistList.value = response.value
        return response
    }

}