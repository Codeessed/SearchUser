package com.example.searchuser.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.searchuser.R
import com.example.searchuser.data.response.Item
import com.example.searchuser.databinding.SearchItemBinding
import com.example.searchuser.utils.OnClickListener

class SearchPagingAdapter(private val onClickListenerInterface: OnClickListener, private val context: Context): PagingDataAdapter<Item, SearchPagingAdapter.SearchViewHolder>(SearchComparator) {

    inner class SearchViewHolder(private val binding: SearchItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, search: Item){
            binding.searchItemLogin.text = search.login
            binding.searchItemEventUrl.text = search.events_url
            binding.searchItemImg.load(search.avatar_url){
                placeholder(R.drawable.ic_launcher_foreground)
                crossfade(true)
                crossfade(1000)
            }
            binding.searchCard.setOnClickListener {
                onClickListenerInterface.onItemClicked(search)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(position, it)
        }
    }

    object SearchComparator : DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

}