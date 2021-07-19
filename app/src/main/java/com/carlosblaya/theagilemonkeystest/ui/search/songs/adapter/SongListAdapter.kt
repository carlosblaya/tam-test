package com.carlosblaya.theagilemonkeystest.ui.search.songs.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.carlosblaya.theagilemonkeystest.R
import com.carlosblaya.theagilemonkeystest.databinding.ItemSongBinding
import com.carlosblaya.theagilemonkeystest.domain.model.Song
import com.carlosblaya.theagilemonkeystest.ui.search.songs.FragmentSongs
import com.carlosblaya.theagilemonkeystest.ui.search.songs.SongsViewModel
import com.carlosblaya.theagilemonkeystest.util.fromUrl
import com.carlosblaya.theagilemonkeystest.util.gone
import com.carlosblaya.theagilemonkeystest.util.show


class SongListAdapter(private val viewModel: SongsViewModel, private val fragmentSongs: FragmentSongs, private val context: Context, private val list: MutableList<Song>, private val onItemClick: (item: Song, position:Int) -> Unit)
    : RecyclerView.Adapter<SongListAdapter.AlbumViewHolder>() {

    private val pulseReverse: Animation = AnimationUtils.loadAnimation(
        context,
        R.anim.heartbeat_reverse
    )

    private val pulseBig: Animation = AnimationUtils.loadAnimation(
        context,
        R.anim.heartbeat_big
    )

    class AlbumViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun render(fragmentSongs:FragmentSongs, position: Int, songListAdapter: SongListAdapter, pulse:Animation, pulseReverse:Animation, viewModel: SongsViewModel, context: Context, item: Song, onItemClick: (item: Song, position:Int) -> Unit) {

            if(item.trackName != null){
                binding.tvNameSong.text = item.trackName
            }
            binding.ivAlbum.fromUrl(item.artworkUrl100 ?: "")

            if(item.kind == Song.KIND_TYPE_MUSIC_VIDEO){
                binding.ivVideo.show()
            }else{
                binding.ivVideo.gone()
            }

            itemView.setOnClickListener {
                itemView.startAnimation(pulseReverse)
                onItemClick(item,position)
            }

            if(viewModel.checkIfLikeExist(item.trackId)){
                item.isLiked = true
                binding.ivFavourite.setImageResource(R.drawable.outline_favorite_24)
                binding.ivFavourite.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.colorLike
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }else{
                item.isLiked = false
                binding.ivFavourite.setImageResource(R.drawable.outline_favorite_border_24)
                binding.ivFavourite.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }

            binding.llLike.setOnClickListener {
                if(item.isLiked){
                    viewModel.deleteLike(item)
                    item.isLiked = false
                }else{
                    viewModel.saveLike(item)
                    item.isLiked = true
                }
                binding.ivFavourite.startAnimation(pulse)
                songListAdapter.notifyItemChanged(position)
                fragmentSongs.setStatusSongLiked(item)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val item = list[position]
        holder.render(fragmentSongs,position,this,pulseBig,pulseReverse,viewModel,context, item, onItemClick)
    }

    fun addAll(itemList: MutableList<Song>) {
        list.addAll(itemList)
        notifyDataSetChanged()
    }

}