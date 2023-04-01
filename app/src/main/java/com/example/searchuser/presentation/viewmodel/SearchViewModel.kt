package com.example.searchuser.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchuser.data.SearchApi
import com.example.searchuser.data.response.SearchUserResponse
import com.example.searchuser.domain.RepositoryInterface
import com.example.searchuser.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
//    private val api: SearchApi,
    private val repositoryInterface: RepositoryInterface
) : ViewModel() {

    private val _searchData: MutableStateFlow<SearchEvents> = MutableStateFlow(SearchEvents.Empty)
    val searchData = _searchData.asStateFlow()

//    val images: Flow<PagingData<PhotoResponseItem>> = Pager(
//        config = PagingConfig(20),
//        pagingSourceFactory = {
//            ImagePagingSource(
//                api
//            )
//        }
//    ).flow.cachedIn(viewModelScope)

    sealed class SearchEvents{
        class SearchSuccess(val searchUserResponse: SearchUserResponse): SearchEvents()
        class Error(val message: String): SearchEvents()
        object Loading: SearchEvents()
        object Empty: SearchEvents()
    }


    fun search(query: String){
        viewModelScope.launch {
            _searchData.value = SearchEvents.Loading
            try{
                when(val result = repositoryInterface.searchUsers(query)){
                    is Resource.Success -> {
                        _searchData.value = SearchEvents.SearchSuccess(result.data!!)
                        Log.d("res", result.data.toString())
                    }
                    is Resource.Error -> {
                        _searchData.value = SearchEvents.Error(result.message!!)
                        Log.d("res", result.message)
                    }
                }
            }catch (e: java.lang.Exception){
                when(e){
                    is IOException -> _searchData.value = SearchEvents.Error("Cannot connect to Internet")
                    else -> _searchData.value = SearchEvents.Error(e.message.toString())
                }
            }

        }
    }
}