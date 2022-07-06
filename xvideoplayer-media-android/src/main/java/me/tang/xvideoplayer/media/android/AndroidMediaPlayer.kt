package me.tang.xvideoplayer.media.android

import android.content.Context
import android.media.MediaPlayer
import android.media.TimedText
import android.net.Uri
import android.text.TextUtils
import android.view.Surface
import android.view.SurfaceHolder
import me.tang.xvideoplayer.AbstractMediaPlayer
import me.tang.xvideoplayer.XTimedText
import java.io.FileDescriptor
import java.lang.ref.WeakReference

class AndroidMediaPlayer : AbstractMediaPlayer() {

    private val mInternalMediaPlayer by lazy { MediaPlayer()  }
    private val mInternalListenerAdapter by lazy { AndroidMediaPlayerListenerHolder(this) }
    private var mIsReleased = false
    private var mDataSource: String? = null

    val internalMediaPlayer get() = mInternalMediaPlayer

    override fun setDisplay(sh: SurfaceHolder) {
        synchronized(this) {
            if (!mIsReleased)
                mInternalMediaPlayer.setDisplay(sh)
        }
    }

    override fun setDataSource(context: Context, uri: Uri) =
        mInternalMediaPlayer.setDataSource(context, uri)

    override fun setDataSource(context: Context, uri: Uri, headers: Map<String?, String?>) =
        mInternalMediaPlayer.setDataSource(context, uri, headers)

    override fun setDataSource(fd: FileDescriptor) =
        mInternalMediaPlayer.setDataSource(fd)

    override fun setDataSource(path: String) {
        mDataSource = path
        val uri = Uri.parse(path)
        val scheme = uri.scheme
        if (!TextUtils.isEmpty(scheme) && scheme.equals("file", ignoreCase = true)) {
            mInternalMediaPlayer.setDataSource(uri.path)
        } else {
            mInternalMediaPlayer.setDataSource(path)
        }
    }

    override fun getDataSource(): String? = mDataSource

    override fun prepareAsync() = mInternalMediaPlayer.prepareAsync()

    override fun start() = mInternalMediaPlayer.start()

    override fun stop() = mInternalMediaPlayer.stop()

    override fun pause() = mInternalMediaPlayer.pause()

    override fun setScreenOnWhilePlaying(screenOn: Boolean) =
        mInternalMediaPlayer.setScreenOnWhilePlaying(screenOn)

    override fun getVideoWidth(): Int = mInternalMediaPlayer.videoWidth

    override fun getVideoHeight(): Int = mInternalMediaPlayer.videoHeight

    override fun isPlaying(): Boolean = mInternalMediaPlayer.isPlaying

    override fun seekTo(msec: Long) = mInternalMediaPlayer.seekTo(msec.toInt())

    override fun getCurrentPosition(): Long = mInternalMediaPlayer.currentPosition.toLong()

    override fun getDuration(): Long = mInternalMediaPlayer.duration.toLong()

    override fun release() {
        mIsReleased = true
        mInternalMediaPlayer.release()
        resetListeners()
        attachInternalListeners()
    }

    override fun reset() {
        mInternalMediaPlayer.reset()
        resetListeners()
        attachInternalListeners()
    }

    override fun setVolume(leftVolume: Float, rightVolume: Float) =
        mInternalMediaPlayer.setVolume(leftVolume, rightVolume)

    override fun getAudioSessionId(): Int = mInternalMediaPlayer.audioSessionId

    override fun setAudioStreamType(streamtype: Int) =
        mInternalMediaPlayer.setAudioStreamType(streamtype)

    override fun getVideoSarNum(): Int = 1

    override fun getVideoSarDen(): Int = 1

    override fun setLooping(looping: Boolean) =
        mInternalMediaPlayer.setLooping(looping)

    override fun isLooping(): Boolean = mInternalMediaPlayer.isLooping

    override fun setSurface(surface: Surface?) =
        mInternalMediaPlayer.setSurface(surface)

    /*--------------------
     * Listeners adapter
     */
    private fun attachInternalListeners() {
        mInternalMediaPlayer.setOnPreparedListener(mInternalListenerAdapter)
        mInternalMediaPlayer
            .setOnBufferingUpdateListener(mInternalListenerAdapter)
        mInternalMediaPlayer.setOnCompletionListener(mInternalListenerAdapter)
        mInternalMediaPlayer
            .setOnSeekCompleteListener(mInternalListenerAdapter)
        mInternalMediaPlayer
            .setOnVideoSizeChangedListener(mInternalListenerAdapter)
        mInternalMediaPlayer.setOnErrorListener(mInternalListenerAdapter)
        mInternalMediaPlayer.setOnInfoListener(mInternalListenerAdapter)
        mInternalMediaPlayer.setOnTimedTextListener(mInternalListenerAdapter)
    }

    private class AndroidMediaPlayerListenerHolder(mp: AndroidMediaPlayer) :
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnTimedTextListener {

        val mWeakMediaPlayer: WeakReference<AndroidMediaPlayer>

        init {
            mWeakMediaPlayer = WeakReference<AndroidMediaPlayer>(mp)
        }

        override fun onPrepared(mp: MediaPlayer?) {
            mWeakMediaPlayer.get()?.notifyOnPrepared()
        }

        override fun onCompletion(mp: MediaPlayer?) {
            mWeakMediaPlayer.get()?.notifyOnCompletion()
        }

        override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
            mWeakMediaPlayer.get()?.notifyOnBufferingUpdate(percent)
        }

        override fun onSeekComplete(mp: MediaPlayer?) {
            mWeakMediaPlayer.get()?.notifyOnSeekComplete()
        }

        override fun onVideoSizeChanged(mp: MediaPlayer?, width: Int, height: Int) {
            mWeakMediaPlayer.get()?.notifyOnVideoSizeChanged(width, height, 1, 1)
        }

        override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
            return mWeakMediaPlayer.get()?.notifyOnError(what, extra) ?: false
        }

        override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
            return mWeakMediaPlayer.get()?.notifyOnInfo(what, extra) ?: false
        }

        override fun onTimedText(mp: MediaPlayer?, text: TimedText?) {
            text?.let {
                mWeakMediaPlayer.get()?.notifyOnTimedText(XTimedText(it.bounds, it.text))
                return@let
            }
            mWeakMediaPlayer.get()?.notifyOnTimedText(null)
        }

    }
}