package com.carlosblaya.theagilemonkeystest.repository

import com.carlosblaya.theagilemonkeystest.data.network.ResponseHandler
import com.carlosblaya.theagilemonkeystest.data.network.services.SongsApiInterface
import com.carlosblaya.theagilemonkeystest.data.response.SongListResponse
import com.carlosblaya.theagilemonkeystest.domain.repository.SongListRepository
import com.carlosblaya.theagilemonkeystest.util.Konsts
import com.carlosblaya.theagilemonkeystest.util.Resource
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException

@RunWith(JUnit4::class)
class SongRepositoryTest {
    private val responseHandler = ResponseHandler()
    private lateinit var songApiInterface:SongsApiInterface
    private lateinit var repository: SongListRepository
    private val validIDAlbum:Long = 1440854851 // Artist:Jack Johnson - Album: Sleep Through the Static
    private val invalidIDAlbum:Long = 20294556 // ??
    private val songListJson = SongListResponse(listOf())

    private val albumSongsResponse = Resource.success(songListJson)
    private val errorResponse = Resource.error("Not Found", null)

    @Before
    fun setUp() {
        songApiInterface = mock()
        val mockException: HttpException = mock()
        whenever(mockException.code()).thenReturn(404)
        runBlocking {
            whenever(songApiInterface.getAlbumSongs(eq(invalidIDAlbum),eq(Konsts.ENTITY_SONG))).thenThrow(mockException)
            whenever(songApiInterface.getAlbumSongs(eq(validIDAlbum),eq(Konsts.ENTITY_SONG))).thenReturn(songListJson)
        }
        repository = SongListRepository(
            songApiInterface,
            responseHandler
        )
    }

    @Test
    fun testValidAlbum() =
        runBlocking {
            assertEquals(albumSongsResponse, repository.getAlbumSongs(validIDAlbum))
        }

    @Test
    fun testNotFoundAlbum() =
        runBlocking {
            assertEquals(errorResponse, repository.getAlbumSongs(invalidIDAlbum))
        }
}