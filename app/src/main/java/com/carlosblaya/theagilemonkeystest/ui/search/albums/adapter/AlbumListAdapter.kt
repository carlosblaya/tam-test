package com.carlosblaya.theagilemonkeystest.ui.search.albums.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.carlosblaya.theagilemonkeystest.R
import com.carlosblaya.theagilemonkeystest.databinding.ItemAlbumBinding
import com.carlosblaya.theagilemonkeystest.domain.model.Album
import com.carlosblaya.theagilemonkeystest.util.DateFormater
import com.carlosblaya.theagilemonkeystest.util.fromUrl


class AlbumListAdapter(private val context: Context, private val list: MutableList<Album>, private val onItemClick: (item: Album) -> Unit)
    : RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>() {

    private val pulseReverse: Animation = AnimationUtils.loadAnimation(
        context,
        R.anim.heartbeat_reverse
    )

    class AlbumViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun render(pulseReverse:Animation, item: Album, onItemClick: (item: Album) -> Unit) {

            if(item.collectionName != null){
                binding.tvNameAlbum.text = item.collectionName
            }
            binding.ivImageAlbum.fromUrl(item.artworkUrl100 ?: "")
            binding.tvReleaseDate.text = DateFormater.formatDateFrom(item.releaseDate,DateFormater.DATE_TIMEZONE,DateFormater.YEAR)

            itemView.setOnClickListener {
                binding.ivImageAlbum.startAnimation(pulseReverse)
                onItemClick(item)
            }

        }

    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val item = list[position]
        holder.render(pulseReverse, item, onItemClick)
    }

    fun addAll(itemList: MutableList<Album>) {
        list.addAll(itemList)
        notifyDataSetChanged()
    }
}