package com.example.searchuser.data

import com.example.searchuser.data.response.SearchUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search/users")
    suspend fun searchUser(
//        @Query("page") page: Int?,
//        @Query("per_page") per_page: Int?,
        @Query("q") query: String
    ): Response<SearchUserResponse>
}