package com.carlosblaya.theagilemonkeystest.ui.search.songs

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.carlosblaya.theagilemonkeystest.R
import com.carlosblaya.theagilemonkeystest.data.response.mapper.SongListMapper
import com.carlosblaya.theagilemonkeystest.databinding.FragmentAlbumSongsBinding
import com.carlosblaya.theagilemonkeystest.ui.base.BaseFragment
import com.carlosblaya.theagilemonkeystest.ui.main.MainActivity
import com.carlosblaya.theagilemonkeystest.domain.model.Album
import com.carlosblaya.theagilemonkeystest.domain.model.Song
import com.carlosblaya.theagilemonkeystest.ui.search.songs.adapter.SongListAdapter
import com.carlosblaya.theagilemonkeystest.ui.video.VideoActivity
import com.carlosblaya.theagilemonkeystest.util.*
import com.carlosblaya.theagilemonkeystest.util.views.SongListMarginDecoration
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class FragmentSongs : BaseFragment<FragmentAlbumSongsBinding, SongsViewModel>() {

    override val viewModel: SongsViewModel by viewModel()

    override fun layout(): Int = R.layout.fragment_album_songs

    lateinit var albumSongsAdapter: SongListAdapter

    var songListMapper: SongListMapper = SongListMapper()

    private val itemDecorator by lazy {
        SongListMarginDecoration(
            requireContext(),
            R.dimen.spacing_small
        )
    }

    override fun init() {
        initView()
        getSongsFromAlbumObserver()
    }

    private fun initView() {
        (requireActivity() as MainActivity).setTitleHeadbar(requireArguments().getString(
            Album.KEY_COLLECTION_NAME).toString())
        (requireActivity() as MainActivity).getTitleHeader().slideToRight()
        (requireActivity() as MainActivity).getImageBack().fadeIn()

        albumSongsAdapter = SongListAdapter(viewModel,this,requireContext(), mutableListOf()) { song,position ->
            if(song.previewUrl != null && song.previewUrl != "") {
                    if(song.kind == Song.KIND_TYPE_MUSIC_VIDEO){
                        (requireActivity() as MainActivity).checkIfAudioIsRunning()
                        val intent = Intent(requireContext(), VideoActivity::class.java)
                        intent.putExtra(Song.KEY_PREVIEW_URL, song.previewUrl)
                        startActivity(intent)
                    }else{
                        (requireActivity() as MainActivity).connectPlayerService(song,position,albumSongsAdapter)
                    }
            }
        }
        binding.rvSongs.addItemDecoration(itemDecorator)
        binding.rvSongs.adapter = albumSongsAdapter
    }

    /**
     * Get album's songs
     */
    private fun getSongsFromAlbumObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAlbumSongs(requireArguments().getLong(Album.KEY_COLLECTION_ID)).observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.loading.gone()
                        val songsListJson = it.data
                        if(songsListJson != null){
                            if(songsListJson.songs!!.isEmpty()){
                                binding.rvSongs.gone()
                                binding.empty.show()
                            }else{
                                val listSongs = songListMapper.toSongList(songsListJson.songs).filter { song -> song.wrapperType != "collection" }
                                albumSongsAdapter.addAll(listSongs.toMutableList())

                                if(listSongs.isEmpty()){ //List was 1 element, but not desired wrapperType
                                    binding.rvSongs.gone()
                                    binding.empty.show()
                                }

                            }
                        }else{
                            binding.rvSongs.gone()
                            binding.empty.show()
                        }
                    }
                    Status.ERROR -> {
                        binding.rvSongs.gone()
                        binding.loading.gone()
                        binding.empty.show()
                    }
                    Status.LOADING -> {
                        binding.loading.show()
                    }
                }
            })
        }
    }

    /**
     * Communication with MainActivity to set player bottom view same status that clicked in adapter
     */
    fun setStatusSongLiked(song:Song){
        (requireActivity() as MainActivity).setSameStatusPlayerViewAsAdapterLiked(song)
    }

}