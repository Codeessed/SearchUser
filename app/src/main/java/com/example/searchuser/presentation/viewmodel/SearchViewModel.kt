package com.example.searchuser.presentation.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.*
import com.example.searchuser.data.SearchApi
import com.example.searchuser.data.response.Item
import com.example.searchuser.data.response.SearchUserResponse
import com.example.searchuser.domain.RepositoryInterface
import com.example.searchuser.pager.SearchPagingSource
import com.example.searchuser.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
) : ViewModel() {

    private val currentQuery = MutableLiveData("")
    val search = currentQuery.switchMap {
        repositoryInterface.searchUsers(it).cachedIn(viewModelScope).asLiveData()
    }

    private val _textState: MutableStateFlow<SearchEvents> = MutableStateFlow(SearchEvents.Empty)
    val textState = _textState.asStateFlow()

    sealed class SearchEvents{
        object NotEmpty: SearchEvents()
        object Empty: SearchEvents()
    }

    fun searchUser(text: String){
        _textState.value = SearchEvents.NotEmpty
        currentQuery.value = text
    }



}