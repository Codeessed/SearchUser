package com.example.searchuser.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.searchuser.databinding.SearchLoadStateFooterBinding

class SearchLoadStateAdapter(private val retry:() -> Unit): LoadStateAdapter<SearchLoadStateAdapter.LoadStateViewHolder>() {
    inner class LoadStateViewHolder(private val binding: SearchLoadStateFooterBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(loadState: LoadState){
            binding.apply {
                footerPbar.isVisible = loadState is LoadState.Loading
                footerErrorMsg.apply {
//                    text = (loadState as LoadState.Error).error.message
                    isVisible = loadState !is LoadState.Loading
                }
                footerRetry.isVisible = loadState !is LoadState.Loading
            }
        }
        init {
            binding.footerRetry.setOnClickListener {
                retry.invoke()
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(SearchLoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}