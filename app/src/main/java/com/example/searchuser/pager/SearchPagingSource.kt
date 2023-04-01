package com.example.searchuser.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchuser.data.SearchApi
import com.example.searchuser.data.response.Item

class SearchPagingSource(
    private val search: SearchApi
): PagingSource<Int, Item>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val pageNumber = params.key ?: 1
        return try {
            val response = search.searchUser("make")
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
        TODO("Not yet implemented")
    }
}