package me.tang.xvideoplayer.controller

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import me.tang.xvideoplayer.XVideoPlayer
import me.tang.xvideoplayer.XVideoPlayerController
import me.tang.xvideoplayer.controller.databinding.VideoPlayerControllerBinding

class XDefaultVideoPlayerController : XVideoPlayerController {

    private lateinit var binding: VideoPlayerControllerBinding

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(context)
    }

    override fun updateProgress(currentDuration: Long) {
        val totalDuration = videoPlayer.media.getDuration()
        val progress = currentDuration *  Int.MAX_VALUE / totalDuration
        Log.d(XVideoPlayer.TAG, "updateProgress currentDuration:$currentDuration, totalDuration:$totalDuration, progress:$progress")
        binding.sbSeek.progress = progress.toInt()
    }

    private fun init(context: Context) {
        binding = VideoPlayerControllerBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

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