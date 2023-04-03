package com.example.searchuser.domain

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.searchuser.data.SearchApi
import com.example.searchuser.data.response.Item
import com.example.searchuser.pager.SearchPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val searchApi: SearchApi
): RepositoryInterface {
    override fun searchUsers(query: String): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 1),
            pagingSourceFactory = {
                SearchPagingSource(
                    query,
                    searchApi
                )
            }
        ).flow
    }
}