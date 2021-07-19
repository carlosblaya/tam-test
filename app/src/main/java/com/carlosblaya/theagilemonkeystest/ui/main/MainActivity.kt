package com.carlosblaya.theagilemonkeystest.ui.main

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.carlosblaya.theagilemonkeystest.R
import com.carlosblaya.theagilemonkeystest.databinding.ActivityMainBinding
import com.carlosblaya.theagilemonkeystest.domain.model.Song
import com.carlosblaya.theagilemonkeystest.ui.search.albums.FragmentAlbums
import com.carlosblaya.theagilemonkeystest.ui.search.artists.FragmentSearchArtists
import com.carlosblaya.theagilemonkeystest.ui.search.songs.adapter.SongListAdapter
import com.carlosblaya.theagilemonkeystest.util.*
import com.carlosblaya.theagilemonkeystest.util.streaming.PlayerService
import com.carlosblaya.theagilemonkeystest.util.streaming.player.PlayerHolder
import com.google.android.exoplayer2.Player
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModel()
    private val binding by viewBinding(ActivityMainBinding::inflate)

    //Streaming Audio
    var mPlayerHolder: PlayerHolder? = null
    var mCurrentSongPlaying: Song? = null
    var binded = false;

    //Animation
    var pulse: Animation? = null
    var discAnimation: ObjectAnimator? = null
    var actualColorLayoutPlayer:Int? = Color.BLACK

    /**
     * Receiver to get back to app if we click on audio notification
     */
    class LaunchPlayerBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notifyIntent = Intent(context, MainActivity::class.java)
            notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(notifyIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
        setupClicks()
    }

    private fun setupClicks() {
        binding.headbar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupViews() {
        FragmentUtil.changeMainFragment(this, FragmentUtil.TAG_SEARCH, null)
        pulse = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.heartbeat_big
        )
        discAnimation = ObjectAnimator.ofFloat(
            binding.ivAlbum,
            "rotation", 0f, 360f
        )
        discAnimation!!.repeatCount = ObjectAnimator.INFINITE
        discAnimation!!.repeatMode = ObjectAnimator.RESTART
        discAnimation!!.duration = 2000
        discAnimation!!.interpolator = LinearInterpolator()
    }

    fun setTitleHeadbar(title: String) {
        binding.headbar.tvTitleHeadbar.text = title
    }

    fun getTitleHeader(): TextView {
        return binding.headbar.tvTitleHeadbar
    }

    fun getImageBack(): ImageView {
        return binding.headbar.ivBack
    }

    ////////////////////////////////////
    /////////Streaming Audio////////////
    ////////////////////////////////////

    /**
     * Connection with PlayerService when clicking on a song
     */
    fun connectPlayerService(
        song: Song,
        positionAdapter: Int,
        songListAdapter: SongListAdapter
    ) {
        mCurrentSongPlaying = song

        if(binding.llPlayer.visibility != View.VISIBLE)
            binding.llPlayer.slideToTop()

        setVisibilityPlayerView(song, songListAdapter, positionAdapter)

        if (connection.isServiceConnected()) {
            if (binded) {
                doUnbind()
            }
        }

        //Calling PlayerService and passing data to PlayerNotification
        val intent = Intent(applicationContext, PlayerService::class.java)
        intent.putExtra("title", song.trackName)
        intent.putExtra("image", song.artworkUrl100)
        intent.putExtra("previewUrl", song.previewUrl)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    /**
     * Create our connection to the service to be used in our bindService call.
     */
    private val connection = object : ServiceConnection {

        private var isServiceConnected = false

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceConnected = false
            binded = false
        }

        /**
         * Called after a successful bind with our PlayerService.
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isServiceConnected = true
            binded = true
            if (service is PlayerService.PlayerServiceBinder) {
                mPlayerHolder = service.getPlayerHolderInstance()
                mPlayerHolder!!.audioFocusPlayer.addListener(object :
                    Player.DefaultEventListener() {
                    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                        if (playWhenReady && playbackState == Player.STATE_READY) {
                            binding.equaliser.playAnimation()
                            discAnimation!!.start()
                        } else if (playWhenReady && playbackState == Player.STATE_ENDED) {
                            stopPlayer()
                        } else if (playWhenReady && playbackState == Player.STATE_BUFFERING) {
                            // buffering (plays when data available)
                        } else if (playWhenReady) {
                            // stopped from notification
                            // or ended (plays when seek away from end)
                            stopPlayer()
                        } else {
                            // player paused in any state
                            binding.equaliser.pauseAnimation()
                            discAnimation!!.pause()
                        }
                    }
                })
            }
        }



        fun isServiceConnected(): Boolean {
            return isServiceConnected
        }
    }

    /**
     * Unbind connection and player service
     */
    fun doUnbind() {
        unbindService(connection)
        stopService(Intent(this, PlayerService::class.java))
        binded = false
    }

    /**
     * Setting all values to bottom player view
     */
    fun setVisibilityPlayerView(
        song: Song,
        songListAdapter: SongListAdapter,
        positionAdapter: Int
    ) {

        binding.flContainer.setPadding(0,0,0,80.px) //Space for player view

        binding.equaliser.playAnimation()

        binding.ivAlbum.fromUrl(song.artworkUrl100 ?: "")

        changeBackgroundColorPlayerView(song)

        discAnimation!!.start()

        binding.tvNameArtist.text = song.artistName
        binding.tvNameSong.text = song.trackName
        binding.tvNameSong.isSelected = true

        if (viewModel.checkIfLikeExist(song.trackId)) {
            song.isLiked = true
            setAppearenceLiked()
        } else {
            song.isLiked = false
            setAppearenceNotLiked()
        }

        binding.llLike.setOnClickListener {
            if (song.isLiked) {
                viewModel.deleteLike(song)
                song.isLiked = false
                setAppearenceNotLiked()
            } else {
                viewModel.saveLike(song)
                song.isLiked = true
                setAppearenceLiked()
            }
            binding.ivFavourite.startAnimation(pulse)
            songListAdapter.notifyItemChanged(positionAdapter)
        }
    }

    /**
     * Changing background color of player view with transition using dark vibrant color of cover
     */
    fun changeBackgroundColorPlayerView(song:Song){

        Glide.with(this)
            .asBitmap()
            .load(song.artworkUrl100 ?: "")
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    Palette.Builder(resource).generate {
                        it?.let { palette ->
                            var auxActualColor:Int?  = actualColorLayoutPlayer

                            var colorNotification = palette.getDarkVibrantColor(
                                ContextCompat.getColor(
                                    applicationContext,
                                    R.color.colorPrimaryDark
                                )
                            )

                            actualColorLayoutPlayer = colorNotification

                            val colorAnimation =
                                ValueAnimator.ofObject(
                                    ArgbEvaluator(),
                                    auxActualColor,
                                    colorNotification
                                )
                            colorAnimation.duration = 750 // milliseconds

                            colorAnimation.addUpdateListener { animator ->
                                binding.llPlayer.setBackgroundColor(animator.animatedValue as Int)
                            }
                            colorAnimation.start()
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    /**
     * Stop player view and animations, and unbind connection
     */
    fun stopPlayer() {
        if (mPlayerHolder != null) {
            binding.flContainer.setPadding(0,0,0,0) // Removing space given for player view
            binding.equaliser.cancelAnimation()
            discAnimation!!.cancel()
            mPlayerHolder?.audioFocusPlayer!!.playWhenReady = false
            binding.llPlayer.slideToBottom()
            binding.llPlayer.gone()
            mCurrentSongPlaying = null
            if(binded)
                doUnbind()
        }
    }

    fun checkIfAudioIsRunning() {
        if (binding.llPlayer.visibility == View.VISIBLE) {
            stopPlayer()
        }
    }

    fun setSameStatusPlayerViewAsAdapterLiked(song: Song) {
        if (binding.llPlayer.isVisible) {
            if (mCurrentSongPlaying != null) {
                if (mCurrentSongPlaying!!.trackId == song.trackId) {
                    if (song.isLiked) {
                        song.isLiked = true
                        setAppearenceLiked()
                    } else {
                        song.isLiked = false
                        setAppearenceNotLiked()
                    }
                    binding.ivFavourite.startAnimation(pulse)
                }
            }

        }
    }

    fun setAppearenceLiked() {
        binding.ivFavourite.setImageResource(R.drawable.outline_favorite_24)
        binding.ivFavourite.setColorFilter(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorLike
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    fun setAppearenceNotLiked() {
        binding.ivFavourite.setImageResource(R.drawable.outline_favorite_border_24)
        binding.ivFavourite.setColorFilter(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    override fun onBackPressed() {
        val fm: FragmentManager = supportFragmentManager
        fm.let {
            if (fm.backStackEntryCount > 1) {
                fm.popBackStackImmediate()
                val f: Fragment? = fm.findFragmentById(R.id.fl_container)
                f.let {
                    if (f is FragmentAlbums) {
                        setTitleHeadbar(f.mArtistNameSelected)
                    } else if (f is FragmentSearchArtists) {
                        setTitleHeadbar(resources.getString(R.string.search))
                        binding.headbar.ivBack.fadeOut()
                    }
                }
            } else {
                finish()
            }
        }
    }
}