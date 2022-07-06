package me.tang.xvideoplayer

import android.annotation.TargetApi
import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.Surface
import android.view.SurfaceHolder
import java.io.FileDescriptor
import java.io.IOException


interface IMediaPlayer {

    val MEDIA_INFO_UNKNOWN get() = 1
    val MEDIA_INFO_STARTED_AS_NEXT get() = 2
    val MEDIA_INFO_VIDEO_RENDERING_START get() = 3
    val MEDIA_INFO_VIDEO_TRACK_LAGGING get() = 700
    val MEDIA_INFO_BUFFERING_START get() = 701
    val MEDIA_INFO_BUFFERING_END get() = 702
    val MEDIA_INFO_NETWORK_BANDWIDTH get() = 703
    val MEDIA_INFO_BAD_INTERLEAVING get() = 800
    val MEDIA_INFO_NOT_SEEKABLE get() = 801
    val MEDIA_INFO_METADATA_UPDATE get() = 802
    val MEDIA_INFO_TIMED_TEXT_ERROR get() = 900
    val MEDIA_INFO_UNSUPPORTED_SUBTITLE get() = 901
    val MEDIA_INFO_SUBTITLE_TIMED_OUT get() = 902

    val MEDIA_INFO_VIDEO_ROTATION_CHANGED get() = 10001
    val MEDIA_INFO_AUDIO_RENDERING_START get() = 10002
    val MEDIA_INFO_AUDIO_DECODED_START get() = 10003
    val MEDIA_INFO_VIDEO_DECODED_START get() = 10004
    val MEDIA_INFO_OPEN_INPUT get() = 10005
    val MEDIA_INFO_FIND_STREAM_INFO get() = 10006
    val MEDIA_INFO_COMPONENT_OPEN get() = 10007
    val MEDIA_INFO_VIDEO_SEEK_RENDERING_START get() = 10008
    val MEDIA_INFO_AUDIO_SEEK_RENDERING_START get() = 10009
    val MEDIA_INFO_MEDIA_ACCURATE_SEEK_COMPLETE get() = 10100

    val MEDIA_ERROR_UNKNOWN get() = 1
    val MEDIA_ERROR_SERVER_DIED get() = 100
    val MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK get() = 200
    val MEDIA_ERROR_IO get() = -1004
    val MEDIA_ERROR_MALFORMED get() = -1007
    val MEDIA_ERROR_UNSUPPORTED get() = -1010
    val MEDIA_ERROR_TIMED_OUT get() = -110

    fun setDisplay(sh: SurfaceHolder)

    @Throws(
        IOException::class,
        IllegalArgumentException::class,
        SecurityException::class,
        IllegalStateException::class
    )
    fun setDataSource(context: Context, uri: Uri)

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Throws(
        IOException::class,
        java.lang.IllegalArgumentException::class,
        SecurityException::class,
        java.lang.IllegalStateException::class
    )
    fun setDataSource(context: Context, uri: Uri, headers: Map<String?, String?>)

    @Throws(
        IOException::class,
        java.lang.IllegalArgumentException::class,
        java.lang.IllegalStateException::class
    )
    fun setDataSource(fd: FileDescriptor)

    @Throws(
        IOException::class,
        java.lang.IllegalArgumentException::class,
        SecurityException::class,
        java.lang.IllegalStateException::class
    )
    fun setDataSource(path: String)

    fun getDataSource(): String?

    @Throws(java.lang.IllegalStateException::class)
    fun prepareAsync()

    @Throws(java.lang.IllegalStateException::class)
    fun start()

    @Throws(java.lang.IllegalStateException::class)
    fun stop()

    @Throws(java.lang.IllegalStateException::class)
    fun pause()

    fun setScreenOnWhilePlaying(screenOn: Boolean)

    fun getVideoWidth(): Int

    fun getVideoHeight(): Int

    fun isPlaying(): Boolean

    @Throws(java.lang.IllegalStateException::class)
    fun seekTo(msec: Long)

    fun getCurrentPosition(): Long

    fun getDuration(): Long

    fun release()

    fun reset()

    fun setVolume(leftVolume: Float, rightVolume: Float)

    fun getAudioSessionId(): Int

    //fun getMediaInfo(): MediaInfo?

    fun setOnPreparedListener(listener: OnPreparedListener?)

    fun setOnCompletionListener(listener: OnCompletionListener?)

    fun setOnBufferingUpdateListener(
        listener: OnBufferingUpdateListener?
    )

    fun setOnSeekCompleteListener(
        listener: OnSeekCompleteListener?
    )

    fun setOnVideoSizeChangedListener(
        listener: OnVideoSizeChangedListener?
    )

    fun setOnErrorListener(listener: OnErrorListener?)

    fun setOnInfoListener(listener: OnInfoListener?)

    fun setOnTimedTextListener(listener: OnTimedTextListener?)

    /*--------------------
     * Listeners
     */
    interface OnPreparedListener {
        fun onPrepared(mp: IMediaPlayer?)
    }

    interface OnCompletionListener {
        fun onCompletion(mp: IMediaPlayer?)
    }

    interface OnBufferingUpdateListener {
        fun onBufferingUpdate(mp: IMediaPlayer?, percent: Int)
    }

    interface OnSeekCompleteListener {
        fun onSeekComplete(mp: IMediaPlayer?)
    }

    interface OnVideoSizeChangedListener {
        fun onVideoSizeChanged(
            mp: IMediaPlayer?, width: Int, height: Int,
            sar_num: Int, sar_den: Int
        )
    }

    interface OnErrorListener {
        fun onError(mp: IMediaPlayer?, what: Int, extra: Int): Boolean
    }

    interface OnInfoListener {
        fun onInfo(mp: IMediaPlayer?, what: Int, extra: Int): Boolean
    }

    interface OnTimedTextListener {
        fun onTimedText(mp: IMediaPlayer?, text: XTimedText?)
    }

    /*--------------------
     * Optional
     */
    fun setAudioStreamType(streamtype: Int)

    fun getVideoSarNum(): Int

    fun getVideoSarDen(): Int

    fun setLooping(looping: Boolean)

    fun isLooping(): Boolean

    /*--------------------
     * AndroidMediaPlayer: JELLY_BEAN
     */
    //fun getTrackInfo(): Array<ITrackInfo?>?

    /*--------------------
     * AndroidMediaPlayer: ICE_CREAM_SANDWICH:
     */
    fun setSurface(surface: Surface?)

    /*--------------------
     * AndroidMediaPlayer: M:
     */
    //fun setDataSource(mediaDataSource: IMediaDataSource?)
}