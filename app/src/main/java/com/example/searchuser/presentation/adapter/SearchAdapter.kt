package com.example.searchuser.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.searchuser.R
import com.example.searchuser.data.response.Item
import com.example.searchuser.databinding.SearchItemBinding
import com.example.searchuser.utils.OnClickListener

class SearchAdapter(private val onClickListenerInterface: OnClickListener, private val context: Context): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(private val binding: SearchItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, context: Context){
            val search = differ.currentList[position]
            binding.searchItemLogin.text = search.login
            binding.searchItemType.text = search.type
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
        return holder.bind(position, context)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var differList = object : DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differList)
}