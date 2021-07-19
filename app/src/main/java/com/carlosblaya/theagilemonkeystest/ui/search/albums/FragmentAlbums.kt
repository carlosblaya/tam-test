package com.carlosblaya.theagilemonkeystest.ui.search.albums

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.carlosblaya.theagilemonkeystest.R
import com.carlosblaya.theagilemonkeystest.data.response.mapper.AlbumListMapper
import com.carlosblaya.theagilemonkeystest.databinding.FragmentArtistAlbumsBinding
import com.carlosblaya.theagilemonkeystest.ui.base.BaseFragment
import com.carlosblaya.theagilemonkeystest.ui.main.MainActivity
import com.carlosblaya.theagilemonkeystest.domain.model.Album
import com.carlosblaya.theagilemonkeystest.domain.model.Artist
import com.carlosblaya.theagilemonkeystest.ui.search.albums.adapter.AlbumListAdapter
import com.carlosblaya.theagilemonkeystest.util.*
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class FragmentAlbums : BaseFragment<FragmentArtistAlbumsBinding, AlbumsViewModel>() {

    override val viewModel: AlbumsViewModel by viewModel()

    override fun layout(): Int = R.layout.fragment_artist_albums

    lateinit var artistAlbumsAdapter: AlbumListAdapter

    var albumListMapper: AlbumListMapper = AlbumListMapper()

    var mArtistNameSelected:String = ""

    override fun init() {
        initView()
        getAlbumsFromArtistObserver()
    }

    private fun initView() {
        mArtistNameSelected = requireArguments().getString(Artist.KEY_ARTIST_NAME).toString()
        (requireActivity() as MainActivity).setTitleHeadbar(mArtistNameSelected)
        (requireActivity() as MainActivity).getTitleHeader().slideToRight()
        (requireActivity() as MainActivity).getImageBack().fadeIn()

        artistAlbumsAdapter = AlbumListAdapter(requireContext(), mutableListOf()) { album ->
            val bundle = Bundle()
            bundle.putLong(Album.KEY_COLLECTION_ID, album.collectionId)
            bundle.putString(Album.KEY_COLLECTION_NAME, album.collectionName)
            FragmentUtil.changeMainFragment(
                requireActivity() as MainActivity,
                FragmentUtil.TAG_SONG, bundle
            )
        }
        binding.rvAlbums.adapter = artistAlbumsAdapter
    }

    /**
     * Get artist's albums
     */
    private fun getAlbumsFromArtistObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getArtistAlbums(requireArguments().getLong(Artist.KEY_ARTIST_ID)).observe(viewLifecycleOwner, {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.loading.gone()
                        val albumListJson = it.data
                        if(albumListJson != null){
                            if(albumListJson.albums!!.isEmpty()){
                                binding.empty.show()
                                binding.rvAlbums.gone()
                            }else{
                                val listAlbums = albumListMapper.toAlbumList(albumListJson.albums).filter { album -> album.wrapperType != "artist"}.sortedByDescending { album -> album.releaseDate }
                                artistAlbumsAdapter.addAll(listAlbums.toMutableList())

                                if(listAlbums.isEmpty()){ //List was 1 element, but not desired wrapperType
                                    binding.rvAlbums.gone()
                                    binding.empty.show()
                                }
                            }
                        }else{
                            binding.empty.show()
                            binding.rvAlbums.gone()
                        }
                    }
                    Status.ERROR -> {
                        binding.empty.show()
                        binding.rvAlbums.gone()
                        binding.loading.gone()
                    }
                    Status.LOADING -> {
                        binding.loading.show()
                    }
                }
            })
        }
    }

}