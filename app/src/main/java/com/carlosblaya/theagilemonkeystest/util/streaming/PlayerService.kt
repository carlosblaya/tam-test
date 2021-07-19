/*
 * Copyright 2018 Filippo Beraldo. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.carlosblaya.theagilemonkeystest.util.streaming

import android.app.IntentService
import android.app.Notification
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.carlosblaya.theagilemonkeystest.R
import com.carlosblaya.theagilemonkeystest.util.streaming.player.PlayerHolder
import com.carlosblaya.theagilemonkeystest.util.streaming.player.PlayerModule
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.Player

class PlayerService : IntentService("tamplayerservice"), PlayerNotificationManager.NotificationListener {

    companion object {
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL = "tamplayer_channel"
    }

    private lateinit var playerHolder: PlayerHolder

    lateinit var playerNotificationManager: PlayerNotificationManager

    private lateinit var mTitle: String
    private lateinit var mImage: String
    private var mPreviewUrl: String = ""

    override fun onCreate() {
        super.onCreate()

        // Build a player holder
        playerHolder = PlayerModule.getPlayerHolder(this)

        /** Build a notification manager for our player, set a notification listener to this,
         * and assign the player just created.
         *
         * It is very important to note we need to get a [PlayerNotificationManager] instance
         * via the [PlayerNotificationManager.createWithNotificationChannel] because targeting Android O+
         * when building a notification we need to create a channel to which assign it.
         */
/*        playerNotificationManager = PlayerModule.getPlayerNotificationManager(idCategory,this).also {
            it.setNotificationListener(this)
            it.setPlayer(playerHolder.audioFocusPlayer)
        }*/
    }


    override fun onBind(intent: Intent?): IBinder {

        mTitle = intent!!.getStringExtra("title").toString()
        mImage = intent.getStringExtra("image").toString()
        mPreviewUrl = intent.getStringExtra("previewUrl").toString()

        playerNotificationManager = PlayerModule.getPlayerNotificationManager(this, mTitle, mImage).also {
            it.setNotificationListener(this)
            it.setPlayer(playerHolder.audioFocusPlayer)
        }

        playerNotificationManager.setControlDispatcher(object : DefaultControlDispatcher() {
            override fun dispatchStop(player: Player, reset: Boolean): Boolean {
                //Do whatever you want on stop button click.
                Log.e("AudioPlayerService","Notification stop button clicked")
                onUnbind(intent)
                player.stop()
                return false
            }
        })

        if(mImage.isNotBlank())
            loadBitmap(mImage)

        intent.let {
            playerHolder.start(mPreviewUrl)
        }
        return PlayerServiceBinder()
    }

    private fun loadBitmap(url: String) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                    ) {
                        Palette.Builder(resource).generate {
                            it?.let { palette ->
                                var colorNotification = palette.getDominantColor(ContextCompat.getColor(this@PlayerService, R.color.colorPrimary))
                                playerNotificationManager.setColor(colorNotification)
                            }
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        playerNotificationManager.setPlayer(null)
        playerHolder.release()
    }

    /** NotificationListener callbacks, we get these calls when our [playerNotificationManager]
     * dispatches them, subsequently to our [PlayerNotificationManager.setPlayer] call.
     *
     * This way we can make our service a foreground service given a notification which was built
     * [playerNotificationManager].
     */
    override fun onNotificationCancelled(notificationId: Int) {}

    override fun onNotificationStarted(notificationId: Int, notification: Notification?) {
        startForeground(notificationId, notification)
    }

    inner class PlayerServiceBinder : Binder() {
        fun getPlayerHolderInstance() = playerHolder
    }

    override fun onHandleIntent(intent: Intent?) {}

    override fun onUnbind(intent: Intent?): Boolean {
        stopService(intent)
        return super.onUnbind(intent)
    }
}
