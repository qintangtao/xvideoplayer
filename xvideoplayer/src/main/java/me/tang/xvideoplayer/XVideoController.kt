package me.tang.xvideoplayer

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

abstract class XVideoController : FrameLayout{

    private val mainScope = MainScope()

    private var _videoPlayer: XVideoPlayer? = null
    val videoPlayer get() = _videoPlayer!!

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {

    }

    fun setVideoPlayer(videoPlayer: XVideoPlayer) {
        _videoPlayer = videoPlayer
    }

    fun startUpdateProgressTimer() {
        flow {
            while (true) {
                emit(videoPlayer.mediaPlayer.getCurrentDuration())
                delay(200)
            }

        }.flowOn(Dispatchers.IO)
        .onEach {
            updateProgress(it)
        }
        .launchIn(mainScope)
    }

    fun cancelUpdateProgressTimer() {
        mainScope.cancel()
    }


    abstract fun updateProgress(position: Long)

}