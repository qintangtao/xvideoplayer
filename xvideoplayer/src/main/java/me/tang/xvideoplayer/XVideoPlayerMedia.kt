package me.tang.xvideoplayer

import android.view.Surface

interface XVideoPlayerMedia {

    fun start(filename: String, surface: Surface)

    fun stop()

    fun seek(pos: Long)

    fun setVolume(volume: Int)

    fun setSpeed(speed: Float)

    fun setSurface(surface: Surface)

    fun tooglePause()

    fun toogleMute()

    fun fastBack()

    fun fastForward()

    fun getDuration(): Long

    fun getCurrentDuration(): Long

}