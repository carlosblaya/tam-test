package com.carlosblaya.theagilemonkeystest.ui.search.artists

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.carlosblaya.theagilemonkeystest.R
import com.carlosblaya.theagilemonkeystest.databinding.FragmentSearchArtistsBinding
import com.carlosblaya.theagilemonkeystest.domain.model.Artist
import com.carlosblaya.theagilemonkeystest.ui.base.BaseFragment
import com.carlosblaya.theagilemonkeystest.ui.main.MainActivity
import com.carlosblaya.theagilemonkeystest.ui.search.artists.adapter.ArtistListAdapter
import com.carlosblaya.theagilemonkeystest.util.FragmentUtil
import com.carlosblaya.theagilemonkeystest.util.fadeIn
import com.carlosblaya.theagilemonkeystest.util.fadeOut
import com.carlosblaya.theagilemonkeystest.util.showMessage
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class FragmentSearchArtists : BaseFragment<FragmentSearchArtistsBinding, SearchArtistsViewModel>() {

    override val viewModel: SearchArtistsViewModel by viewModel()

    override fun layout(): Int = R.layout.fragment_search_artists

    private val artistListAdapter by lazy {
        ArtistListAdapter {
            val bundle = Bundle()
            bundle.putLong(Artist.KEY_ARTIST_ID, it.artistId.toLong())
            bundle.putString(Artist.KEY_ARTIST_NAME, it.artistName)
            FragmentUtil.changeMainFragment(
                requireActivity() as MainActivity,
                FragmentUtil.TAG_ALBUM, bundle
            )
        }
    }

    override fun init() {
        initView()
        setupClicks()
        initAdapter()
        bindEvents()
    }


    private fun initView() {
        (requireActivity() as MainActivity).setTitleHeadbar(resources.getString(R.string.search))

        // initial recyclerView
        binding.rvArtists.setHasFixedSize(true)
        binding.rvArtists.adapter = artistListAdapter

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    if(s.length == 1) {
                        if(!binding.ivClearSearch.isVisible)
                            binding.ivClearSearch.fadeIn()
                    }
                } else {
                    binding.ivClearSearch.fadeOut()
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.getArtistList(s.toString()).observe(viewLifecycleOwner, {
                        artistListAdapter.submitData(lifecycle, it)
                    })
                }
            }
        })
    }

    private fun setupClicks() {
        binding.ivClearSearch.setOnClickListener {
            binding.etSearch.setText("")
        }
    }

    /**
     * LoadStateListener for paging
     */
    private fun initAdapter() {
        artistListAdapter.addLoadStateListener { loadState ->
            // show empty list
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && artistListAdapter.itemCount == 0
            binding.tvNoResults.isVisible = isListEmpty

            // Only show the list if refresh succeeds.
            binding.rvArtists.isVisible = loadState.source.refresh is LoadState.NotLoading

            // Show loading spinner during initial load or refresh.
            handleLoading(loadState.source.refresh is LoadState.Loading)

            /**
             * loadState.refresh - represents the load state for loading the PagingData for the first time.
             * loadState.prepend - represents the load state for loading data at the start of the list.
             * loadState.append - represents the load state for loading data at the end of the list.
             * */
            // If we have an error, show a toast
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                requireActivity().showMessage(it.error.message.toString())
            }
        }
    }

    private fun bindEvents() {
        with(binding) {
            refreshLayout.setOnRefreshListener {
                artistListAdapter.refresh()
            }
        }
    }

    private fun handleLoading(loading: Boolean) {
        with(binding) {
            refreshLayout.isRefreshing = loading == true
        }
    }
}