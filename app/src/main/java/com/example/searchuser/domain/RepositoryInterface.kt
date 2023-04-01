package com.example.searchuser.domain

import com.example.searchuser.data.response.SearchUserResponse
import com.example.searchuser.utils.Resource


interface RepositoryInterface {

    suspend fun searchUsers(query: String): Resource<SearchUserResponse>

}