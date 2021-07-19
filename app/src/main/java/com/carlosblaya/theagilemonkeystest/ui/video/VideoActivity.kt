package com.carlosblaya.theagilemonkeystest.ui.video

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.carlosblaya.theagilemonkeystest.databinding.ActivityVideoBinding
import com.carlosblaya.theagilemonkeystest.domain.model.Song
import com.carlosblaya.theagilemonkeystest.util.*
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

class VideoActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityVideoBinding::inflate)

    //Streaming Music-Video
    var videoPlayer: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializePlayer()
    }

    /**
     * Initialization ExoPlayer Video with our previewUrl from song via intent extras
     */
    fun initializePlayer() {
        try {

            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()

            val trackSelector: TrackSelector =
                DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))

            videoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

            val videouri = Uri.parse(intent.extras!!.getString(Song.KEY_PREVIEW_URL))

            val dataSourceFactory = DefaultHttpDataSourceFactory("exoplayer_video")

            val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
            val mediaSource: MediaSource =
                ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null)

            binding.videoPlayerView.player = videoPlayer

            (binding.videoPlayerView.player as SimpleExoPlayer?)!!.prepare(mediaSource)
            binding.videoPlayerView.player.playWhenReady = true
        } catch (e: Exception) {
            Log.e("TAG", "Error : $e")
        }
    }

    override fun onBackPressed() {
        videoPlayer!!.stop()
        finish()
    }

    override fun onDestroy() {
        videoPlayer!!.stop()
        super.onDestroy()
    }

}