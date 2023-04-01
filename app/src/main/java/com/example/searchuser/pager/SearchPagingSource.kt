package com.example.searchuser.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchuser.data.SearchApi
import com.example.searchuser.data.response.Item

class SearchPagingSource(
    private val query: String,
    private val search: SearchApi
): PagingSource<Int, Item>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        var pageNumber = params.key ?: 1
        return try {
            val response = search.searchUser(query, pageNumber, 10)
            val pagedResponse = response.body()
            val data = pagedResponse?.items

            val nextPageNumber = pageNumber + 1
            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = nextPageNumber
            )

        }catch (e: java.lang.Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return null
    }
}