package com.carlosblaya.theagilemonkeystest.networking

import com.carlosblaya.theagilemonkeystest.data.network.ResponseHandler
import com.carlosblaya.theagilemonkeystest.data.response.SongListResponse
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

@RunWith(JUnit4::class)
class ResponseHandlerTest {
    lateinit var responseHandler: ResponseHandler

    @Before
    fun setUp() {
        responseHandler = ResponseHandler()
    }

    @Test
    fun testSongNotFound() {
        val httpException = HttpException(Response.error<SongListResponse>(404, mock()))
        val result = responseHandler.handleException<SongListResponse>(httpException)
        assertEquals("Not Found", result.message)
    }

    @Test
    fun testTimeOut() {
        val socketTimeoutException = SocketTimeoutException()
        val result = responseHandler.handleException<SongListResponse>(socketTimeoutException)
        assertEquals("Timeout", result.message)
    }
}