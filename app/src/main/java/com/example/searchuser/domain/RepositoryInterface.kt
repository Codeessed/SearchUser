package com.example.searchuser.domain

import androidx.paging.PagingData
import com.example.searchuser.data.response.Item
import kotlinx.coroutines.flow.Flow


interface RepositoryInterface {

    fun searchUsers(query: String): Flow<PagingData<Item>>

}