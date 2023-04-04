package com.example.searchuser

import com.example.searchuser.data.SearchApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchApiTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var searchApi: SearchApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        searchApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))//We will use MockWebServers url
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApi::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `each request returns 10 per page`() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)
        val response = searchApi.searchUser("a", null, 1, 10)
        mockWebServer.takeRequest()
        Assert.assertEquals(10, response.body()!!.items.size)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

}