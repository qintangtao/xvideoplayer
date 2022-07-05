package me.tang.xvideoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import me.tang.xvideoplayer.controller.XDefaultVideoPlayerController
import me.tang.xvideoplayer.databinding.ActivityMainBinding
import me.tang.xvideoplayer.ffmpeg.XFFmpegVideoPlayerMedia

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        Log.d(XVideoPlayer.TAG, "MainActivity onCreate")

        binding.xVideoPlayer.setMedia(XFFmpegVideoPlayerMedia())
        binding.xVideoPlayer.setController(XDefaultVideoPlayerController(this))

        binding.btnFullScreen.setOnClickListener {
            Log.d(XVideoPlayer.TAG, "MainActivity setOnClickListener")
            binding.xVideoPlayer.enterFullScreen()
        }

        binding.btnTinyWindow.setOnClickListener {
            if (binding.xVideoPlayer.windowMode == XVideoPlayer.WINDOW_MODE_TINY)
                binding.xVideoPlayer.exitTinyWindow()
            else
                binding.xVideoPlayer.enterTinyWindow()
        }

        binding.btnVideoSize.setOnClickListener {
            val w = binding.xVideoPlayer.media.width()
            val h = binding.xVideoPlayer.media.height()
            Log.d(XVideoPlayer.TAG, "MainActivity setVideoSize $w x $h")
            binding.xVideoPlayer.setVideoSize(w, h)
        }

        binding.btnRotation90.setOnClickListener {
            binding.xVideoPlayer.setVideoRotation(90f)
        }

        binding.btnFillParent.setOnClickListener {
            binding.xVideoPlayer.setDisplayType(XTextureView.DISPLAY_TYPE_FILL_PARENT)
        }
        binding.btnFillCrop.setOnClickListener {
            binding.xVideoPlayer.setDisplayType(XTextureView.DISPLAY_TYPE_FILL_SCROP)
        }
        binding.btnOriginal.setOnClickListener {
            binding.xVideoPlayer.setDisplayType(XTextureView.DISPLAY_TYPE_ORIGINAL)
        }
        binding.btnAdapter.setOnClickListener {
            binding.xVideoPlayer.setDisplayType(XTextureView.DISPLAY_TYPE_ADAPTER)
        }

    }

    override fun onBackPressed() {
        Log.d(XVideoPlayer.TAG, "MainActivity onBackPressed")
        if (binding.xVideoPlayer.exitFullScreen())
            return
        if (binding.xVideoPlayer.exitTinyWindow())
            return
        super.onBackPressed()
    }
}