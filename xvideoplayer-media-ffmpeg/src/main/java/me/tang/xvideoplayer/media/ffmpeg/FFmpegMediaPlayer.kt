package me.tang.xvideoplayer.media.ffmpeg

import android.view.Surface
import me.tang.ffmpeg.FFplay
import me.tang.xvideoplayer.XMediaPlayer

class FFmpegMediaPlayer : XMediaPlayer {

    private var handler: Long? = null

    override fun start(filename: String, surface: Surface) {
        handler = FFplay.open(filename, surface)
    }

    override fun stop() {
        handler?.let {
            FFplay.close(it)
            handler = null
        }
    }

    override fun seek(pos: Long) {
        handler?.let {
            FFplay.seek(it, pos)
        }
    }

    override fun setVolume(volume: Int) {
        handler?.let {
            FFplay.setVolume(it, volume.toLong())
        }
    }

    override fun setSpeed(speed: Float) {
        handler?.let {
            FFplay.setRate(it, speed.toInt())
        }
    }

    override fun setSurface(surface: Surface) {
        handler?.let {
            FFplay.setSurface(it, surface)
        }
    }

    override fun tooglePause() {
        handler?.let {
            FFplay.sendEvent(it, FFplay.FF_EVENT_TOOGLE_PAUSE)
        }
    }

    override fun toogleMute() {
        handler?.let {
            FFplay.sendEvent(it, FFplay.FF_EVENT_TOOGLE_MUTE)
        }
    }

    override fun fastBack() {
        handler?.let {
            FFplay.sendEvent(it, FFplay.FF_EVENT_FAST_BACK)
        }
    }

    override fun fastForward() {
        handler?.let {
            FFplay.sendEvent(it, FFplay.FF_EVENT_FAST_FORWARD)
        }
    }

    override fun getDuration(): Long {
        handler?.let {
            return FFplay.duration(it)
        }
        return 0
    }

    override fun getCurrentDuration(): Long {
        handler?.let {
            return FFplay.currentDuration(it)
        }
        return 0
    }

    override fun width(): Int {
        handler?.let {
            return FFplay.width(it)
        }
        return 0
    }

    override fun height(): Int {
        handler?.let {
            return FFplay.height(it)
        }
        return 0
    }
}