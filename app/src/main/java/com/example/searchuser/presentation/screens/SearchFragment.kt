package com.example.searchuser.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var searches: ArrayList<Item>
    private lateinit var pagees: PagingData<String>

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
        }
        binding.topRetry.setOnClickListener {
            searchUser()
        }

        observer(viewmodel.textState){ textState ->
            when(textState){
                is SearchViewModel.SearchEvents.NotEmpty -> {
                    binding.searchEmpty.isVisible = false
                    viewmodel.search.observe(viewLifecycleOwner){ pagedItems ->
                        searchPagedAdapter.submitData(viewLifecycleOwner.lifecycle, pagedItems)
                    }
                }
                else -> {
                    binding.searchEmpty.isVisible = true
                }
            }
        }

        searchPagedAdapter.addLoadStateListener { loadState ->
            binding.apply {
                topPbar.isVisible = loadState.source.refresh is LoadState.Loading
                topRetry.isVisible = loadState.source.refresh is LoadState.Error
                searchRecycler.isVisible = loadState.source.refresh !is LoadState.Loading
                topErrorMsg.isVisible = loadState.source.refresh is LoadState.Error

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
