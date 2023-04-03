package com.example.searchuser.pager

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchuser.data.SearchApi
import com.example.searchuser.data.response.Item

private const val SEARCH_START_PAGE_INDEX = 1
class SearchPagingSource(
    private val query: String,
    private val search: SearchApi
): PagingSource<Int, Item>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val pageNumber = params.key ?: SEARCH_START_PAGE_INDEX
        return try {
            val response = search.searchUser(query, "asc", pageNumber, 10)
            val pagedResponse = response.body()
            val data = pagedResponse?.items
            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = if (pageNumber == SEARCH_START_PAGE_INDEX) null else pageNumber - 1,
                nextKey = if (data.isNullOrEmpty()) null else pageNumber + 1
            )
        }catch (e: java.lang.Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return null
    }
}