package com.carlosblaya.theagilemonkeystest.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.carlosblaya.theagilemonkeystest.R
import com.carlosblaya.theagilemonkeystest.ui.main.MainActivity
import com.carlosblaya.theagilemonkeystest.ui.search.albums.FragmentAlbums
import com.carlosblaya.theagilemonkeystest.ui.search.artists.FragmentSearchArtists
import com.carlosblaya.theagilemonkeystest.ui.search.songs.FragmentSongs


object FragmentUtil {

    val TAG_SEARCH: String = "search"
    val TAG_ALBUM: String = "album"
    val TAG_SONG: String = "song"

    fun changeMainFragment(activity: MainActivity, tag: String, bundle: Bundle?) {
        val fragment = activity.supportFragmentManager.findFragmentByTag(tag)
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        if (fragment == null) {
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in,R.anim.fade_out).add(
                R.id.fl_container, getFragmentByTag(
                    tag,
                    bundle
                )!!, tag
            ).addToBackStack(tag)
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out).show(fragment)
        }
        fragmentTransaction.commit()
    }

    fun getFragmentByTag(tag: String, bundle: Bundle?): Fragment? {
        when (tag) {
            TAG_SEARCH -> return FragmentSearchArtists()
            TAG_ALBUM -> {
                val albumsFagment = FragmentAlbums()
                albumsFagment.arguments = bundle
                return albumsFagment
            }
            TAG_SONG -> {
                val songsFragment = FragmentSongs()
                songsFragment.arguments = bundle
                return songsFragment
            }
        }
        return null
    }
}

