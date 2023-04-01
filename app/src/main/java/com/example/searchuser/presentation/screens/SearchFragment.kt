package com.example.searchuser.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchuser.data.response.Item
import com.example.searchuser.databinding.FragmentSearchBinding
import com.example.searchuser.presentation.adapter.SearchAdapter
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
    private lateinit var searchButton: MaterialButton
    private var searchText: String? = null
//    private lateinit var pagingAdapter: ImagesPagingAdapter
    private lateinit var searches: List<Item>

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
        searchButton = binding.searchBtn
        binding.searchText.addTextChangedListener {
            searchText = it.toString()
            searchButton.isEnabled = !searchText.isNullOrEmpty()
        }
        searchButton.setOnClickListener {
            viewmodel.search(searchText!!)
        }
        observer(viewmodel.searchData){ searchData ->
            when(searchData){
                is SearchViewModel.SearchEvents.SearchSuccess -> {
                    searches = searchData.searchUserResponse.items
                    setSearchRecycler()
                }
                is SearchViewModel.SearchEvents.Error -> {
                    Toast.makeText(requireContext(), searchData.message, Toast.LENGTH_SHORT).show()
                }
                is SearchViewModel.SearchEvents.Loading
                -> {
                    Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                }
                else -> Unit

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setSearchRecycler(){
        searchAdapter = SearchAdapter(this, requireContext())
        searchRecycler = binding.searchRecycler
        searchRecycler.adapter = searchAdapter
        searchRecycler.layoutManager = LinearLayoutManager(requireContext())
        searchAdapter.differ.submitList(searches)
    }

    override fun onItemClicked(pos: Int) {
        Toast.makeText(requireContext(), searches[pos].login, Toast.LENGTH_SHORT).show()
    }

}
