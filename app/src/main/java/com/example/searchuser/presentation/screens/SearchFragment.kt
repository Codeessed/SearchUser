package com.example.searchuser.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchuser.R
import com.example.searchuser.data.response.Item
import com.example.searchuser.databinding.FragmentSearchBinding
import com.example.searchuser.presentation.adapter.SearchAdapter
import com.example.searchuser.presentation.adapter.SearchLoadStateAdapter
import com.example.searchuser.presentation.adapter.SearchPagingAdapter
import com.example.searchuser.presentation.viewmodel.SearchViewModel
import com.example.searchuser.utils.Extensions.observer
import com.example.searchuser.utils.OnClickListener
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment: Fragment(), OnClickListener {


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: SearchViewModel by activityViewModels()

    private lateinit var searchRecycler: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchPagedAdapter: SearchPagingAdapter
    private lateinit var searchButton: MaterialButton
    private var searchText: String? = null
//    private lateinit var pagingAdapter: ImagesPagingAdapter
    private lateinit var searches: ArrayList<Item>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPagedSearchRecycler()
        searchButton = binding.searchBtn
        binding.searchText.addTextChangedListener {
            searchText = it.toString()
            searchButton.isEnabled = !searchText.isNullOrEmpty()
        }

        searchButton.setOnClickListener {
            searchUser()
//            setPagedSearchRecycler()

//            viewLifecycleOwner.lifecycleScope.launch {
//                repeatOnLifecycle(Lifecycle.State.RESUMED){
//                    if (!searchText.isNullOrEmpty()){
//                        viewmodel.search.collectLatest { pagedSearchData ->
//                            searchPagedAdapter.submitData(pagedSearchData)
//                        }
//                    }
//                }
//            }
        }

        viewmodel.search.observe(viewLifecycleOwner){
            searchPagedAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

//        observer(viewmodel.searchData){ searchData ->
//            when(searchData){
//                is SearchViewModel.SearchEvents.SearchSuccess -> {
//                    Toast.makeText(requireContext(), "got to success", Toast.LENGTH_SHORT).show()
////                    searchData.searchUserResponse.map {
//                        searchPagedAdapter.submitData(searchData.searchUserResponse)
////                    }
//                }
//                else -> {
//                    Toast.makeText(requireContext(), "got to else", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
        searchPagedAdapter.addLoadStateListener { loadState ->
//            setPagedSearchRecycler()
            binding.apply {
                topPbar.isVisible = loadState.source.refresh is LoadState.Loading
                topRetry.isVisible = loadState.source.refresh is LoadState.Error
                searchRecycler.isVisible = loadState.source.refresh !is LoadState.Loading
                topErrorMsg.apply {
                    isVisible = loadState.source.refresh is LoadState.Error
//                    text = (loadState as LoadState.Error).error.message
                    setOnClickListener {
                        searchUser()
                    }
                }

            }
        }
//
//        viewLifecycleOwner.lifecycleScope.launch {
////            repeatOnLifecycle(Lifecycle.State.RESUMED){
//                searchPagedAdapter.loadStateFlow.collectLatest{ loadStates ->
//                    when(loadStates.refresh){
//                        is LoadState.Loading -> {
//                            with(binding) {
//                                bottomPbar.isVisible = true
//                                retryBtn.isVisible = false
//                                errorMsg.isVisible = false
//                            }
//                        }
//                        is LoadState.Error -> {
//                            with(binding) {
//                                bottomPbar.isVisible = false
//                                retryBtn.isVisible = true
//                                errorMsg.isVisible = true
//                                errorMsg.text = (loadStates as? LoadState.Error)?.error?.message
//                            }
//                        }
//                        is LoadState.NotLoading -> {
//                            with(binding) {
//                                bottomPbar.isVisible = false
//                                retryBtn.isVisible = false
//                                errorMsg.isVisible = false
//                            }
//                        }
//
//                    }
//                }
//
////            }
//        }





//        observer(viewmodel.searchData){ searchData ->
//            when(searchData){
//                is SearchViewModel.SearchEvents.SearchSuccess -> {
//                    searches = searchData.searchUserResponse.items
//                    setPagedSearchRecycler()
//                }
//                is SearchViewModel.SearchEvents.Error -> {
//                    Toast.makeText(requireContext(), searchData.message, Toast.LENGTH_SHORT).show()
//                }
//                is SearchViewModel.SearchEvents.Loading
//                -> {
//                    Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
//                }
//                else -> Unit
//
//            }
//        }
    }

    private fun searchUser(){
        searchPagedAdapter.refresh()
        viewmodel.searchUser(searchText!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setPagedSearchRecycler(){
        searchRecycler = binding.searchRecycler
        searchRecycler.layoutManager = LinearLayoutManager(requireContext())
        searchPagedAdapter = SearchPagingAdapter(this, requireContext())
        searchRecycler.adapter = searchPagedAdapter.withLoadStateHeaderAndFooter(
            header = SearchLoadStateAdapter{searchPagedAdapter.retry()},
            footer = SearchLoadStateAdapter{searchPagedAdapter.retry()}
        )
    }

    private fun setSearchRecycler(){
        searchAdapter = SearchAdapter(this, requireContext())
        searchRecycler = binding.searchRecycler
        searchRecycler.adapter = searchAdapter
        searchRecycler.layoutManager = LinearLayoutManager(requireContext())
        searchAdapter.differ.submitList(searches)
    }

    override fun onItemClicked(item: Item) {
        val action = SearchFragmentDirections.actionSearchFragmentToSearchDetailsFragment(item)
        findNavController().navigate(action)
    }

}
