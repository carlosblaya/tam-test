package com.carlosblaya.theagilemonkeystest.ui.search.artists.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.carlosblaya.theagilemonkeystest.R
import com.carlosblaya.theagilemonkeystest.databinding.ItemArtistBinding
import com.carlosblaya.theagilemonkeystest.domain.model.Artist
import com.carlosblaya.theagilemonkeystest.util.hideKeyboard


class ArtistListAdapter(private val artistClickListener: (artistItem: Artist) -> Unit) :
    PagingDataAdapter<Artist, ArtistListAdapter.ViewHolder>(DiffUtilCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemArtistBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_artist,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ItemArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        init {

            binding.root.setOnClickListener {
                val artistItem = getItem(absoluteAdapterPosition)
                artistItem?.let {
                    val pulse: Animation = AnimationUtils.loadAnimation(context, R.anim.heartbeat)
                    pulse.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {
                            binding.rlFirstLetter.hideKeyboard()
                        }
                        override fun onAnimationEnd(animation: Animation) {
                            artistClickListener(it)
                        }
                        override fun onAnimationRepeat(animation: Animation) {}
                    })
                    binding.rlFirstLetter.startAnimation(pulse)
                }
            }
        }

        fun bind(item: Artist) {
            with(binding) {
                artistItem = item
            }
            if (item.artistName.isNotEmpty()) {
                binding.tvFirstLetter.text = item.artistName.substring(0, 1)
            }
            binding.tvNameArtist.text = item.artistName
            binding.tvGenreArtist.text = item.primaryGenreName
        }
    }

    object DiffUtilCallBack : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(
            oldItem: Artist,
            newItem: Artist
        ): Boolean {
            return oldItem.artistId == newItem.artistId
        }

        override fun areContentsTheSame(
            oldItem: Artist,
            newItem: Artist
        ): Boolean {
            return oldItem == newItem
        }
    }
}