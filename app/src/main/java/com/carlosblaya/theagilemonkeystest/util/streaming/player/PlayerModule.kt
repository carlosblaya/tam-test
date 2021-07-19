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

package com.carlosblaya.theagilemonkeystest.util.streaming.player

import android.content.Context
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.carlosblaya.theagilemonkeystest.R
import com.carlosblaya.theagilemonkeystest.util.streaming.PlayerService
import com.carlosblaya.theagilemonkeystest.util.streaming.media.DescriptionAdapter

object PlayerModule {
    fun getPlayerHolder(context: Context) = PlayerHolder(context, PlayerState())

    fun getPlayerNotificationManager(context: Context, title: String, image:String): PlayerNotificationManager =
            PlayerNotificationManager.createWithNotificationChannel(
                    context,
                    PlayerService.NOTIFICATION_CHANNEL,
                    R.string.app_name,
                    PlayerService.NOTIFICATION_ID,
                    getDescriptionAdapter(context, title,image)).apply {
                setFastForwardIncrementMs(0)
                setOngoing(false)
                setColor(context.resources.getColor(R.color.colorPrimary,null))
                setColorized(true)
                setSmallIcon(R.drawable.outline_audiotrack_24)
                setUseNavigationActions(true)
                setRewindIncrementMs(0)
                //setStopAction(null)
                //setUsePlayPauseActions(true)
            }

    private fun getDescriptionAdapter(context: Context, title: String, image:String) = DescriptionAdapter(context, title,image)
}