package me.tang.xvideoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import me.tang.xvideoplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        Log.d(XVideoPlayer.TAG, "MainActivity onCreate")

        binding.btnFullScreen.setOnClickListener {
            Log.d(XVideoPlayer.TAG, "MainActivity setOnClickListener")
            binding.xVideoPlayer.enterFullScreen()
        }

        binding.btnTinyWindow.setOnClickListener {
            if (binding.xVideoPlayer.currentMode == XVideoPlayer.MODE_TINY_WINDOW)
                binding.xVideoPlayer.exitTinyWindow()
            else
                binding.xVideoPlayer.enterTinyWindow()
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