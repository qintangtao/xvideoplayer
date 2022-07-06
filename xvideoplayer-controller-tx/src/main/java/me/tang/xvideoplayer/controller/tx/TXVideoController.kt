package me.tang.xvideoplayer.controller.tx

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import me.tang.xvideoplayer.XVideoPlayer
import me.tang.xvideoplayer.XVideoController
import me.tang.xvideoplayer.controller.tx.databinding.TxVideoPlayerControllerBinding

class TXVideoController : XVideoController {

    private lateinit var binding: TxVideoPlayerControllerBinding

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(context)
    }

    override fun updateProgress(currentDuration: Long) {
        val totalDuration = videoPlayer.mediaPlayer.getDuration()
        val progress = currentDuration *  Int.MAX_VALUE / totalDuration
        Log.d(XVideoPlayer.TAG, "updateProgress currentDuration:$currentDuration, totalDuration:$totalDuration, progress:$progress")
        binding.sbSeek.progress = progress.toInt()
    }

    private fun init(context: Context) {
        binding = TxVideoPlayerControllerBinding.inflate(LayoutInflater.from(context), this, true)

        binding.sbSeek.max = Int.MAX_VALUE

        binding.btnPlay.setOnClickListener {
            videoPlayer.start()
        }

        binding.btnProgress.setOnClickListener {
            startUpdateProgressTimer()
        }

        binding.btnFullScreen.setOnClickListener {
            if (videoPlayer.windowMode == XVideoPlayer.WINDOW_MODE_FULLSCREEN)
                videoPlayer.exitFullScreen()
            else
                videoPlayer.enterFullScreen()
        }
    }


}