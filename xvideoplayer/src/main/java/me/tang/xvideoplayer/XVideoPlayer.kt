package me.tang.xvideoplayer

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils

class XVideoPlayer : FrameLayout, TextureView.SurfaceTextureListener{

    companion object {
        @JvmStatic
        val TAG = XVideoPlayer::class.java.simpleName

        const val WINDOW_MODE_NORMAL = 10
        const val WINDOW_MODE_FULLSCREEN = 11
        const val WINDOW_MODE_TINY = 12

        const val DISPLAY_TYPE_ADAPTER = 0
        const val DISPLAY_TYPE_FILL_PARENT = 1
        const val DISPLAY_TYPE_FILL_SCROP = 2
        const val DISPLAY_TYPE_ORIGINAL = 3
    }

    //
    private var _windowMode = WINDOW_MODE_NORMAL
    val windowMode get() = _windowMode

    private var _displayType = DISPLAY_TYPE_ADAPTER
    val displayType get() = _displayType

    //view
    private lateinit var container: FrameLayout

    private var _textureView: XTextureView? = null
    private val textureView get() = _textureView!!

    private var _media: XVideoPlayerMedia? = null
    val media get() = _media!!
//private
    private var _controler: XVideoPlayerController? = null
    private val controller get() = _controler!!

    // surface
    private var _surfaceTexture: SurfaceTexture? = null
    private val surfaceTexture get() = _surfaceTexture!!

    private var _surface: Surface? = null
    private val surface get() = _surface!!

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun start() {
        //media.start("rtsp://admin:br123456789@192.168.1.39:554/avstream", surface)
        media.start("/data/local/tmp/v1080.mp4", surface)
    }

    fun stop() {
        media.stop()
    }


    fun setScale(scale: Float) {
        _textureView?.setScale(scale)
    }

    fun setDisplayType(type: Int) {
        if (displayType != type) {
            _displayType = type
            _textureView?.setDisplayType(type)
        }
    }

    fun setVideoSize(width: Int, height: Int) {
        _textureView?.setVideoSize(width, height)
    }

    fun setVideoRotation(rotation: Float) {
        _textureView?.rotation = rotation
    }


    fun setMedia(media: XVideoPlayerMedia) {
        _media = media
    }

    fun setController(controller2: XVideoPlayerController) {
        _controler?.let {
            container.removeView(it)
        }
        _controler = controller2
        _controler?.let {
            it.setVideoPlayer(this)
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
            container.addView(it, params)
        }
    }

    fun enterFullScreen() {
        if (windowMode == WINDOW_MODE_FULLSCREEN)
            return

        XUtil.hideActionBar(this.context)
        XUtil.setOrientation(this.context, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        val contentView = XUtil.scanForActivity(this.context)?.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        check(contentView != null) { "ID_ANDROID_CONTENT not found" }

        if (windowMode == WINDOW_MODE_TINY)
            contentView.removeView(container)
        else
            removeView(container)

        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        contentView.addView(container, params)

        _windowMode = WINDOW_MODE_FULLSCREEN
    }

    fun exitFullScreen(): Boolean{
        if (windowMode != WINDOW_MODE_FULLSCREEN)
            return false

        XUtil.showActionBar(this.context)
        XUtil.setOrientation(this.context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        val contentView = XUtil.scanForActivity(this.context)?.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        check(contentView != null) { "ID_ANDROID_CONTENT not found" }

        contentView.removeView(container)

        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        addView(container, params)

        _windowMode = WINDOW_MODE_NORMAL
        return true
    }

    fun enterTinyWindow() {
        if (windowMode == WINDOW_MODE_TINY)
            return

        removeView(container)

        val contentView = XUtil.scanForActivity(this.context)?.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        check(contentView != null) { "ID_ANDROID_CONTENT not found" }

        val params = FrameLayout.LayoutParams(
            (ScreenUtils.getScreenWidth() * 0.5f).toInt(),
            (ScreenUtils.getScreenWidth() * 0.5f * 9f / 16f).toInt()).apply {
                gravity = Gravity.BOTTOM or Gravity.END
            rightMargin = SizeUtils.dp2px(14f)
            bottomMargin = SizeUtils.dp2px(14f)
        }
        contentView.addView(container, params)

        _windowMode = WINDOW_MODE_TINY
    }

    fun exitTinyWindow(): Boolean{
        if (windowMode != WINDOW_MODE_TINY)
            return false

        val contentView = XUtil.scanForActivity(this.context)?.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        check(contentView != null) { "ID_ANDROID_CONTENT not found" }

        contentView.removeView(container)

        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        addView(container, params)

        _windowMode = WINDOW_MODE_NORMAL
        return true
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        container = FrameLayout(context).apply {
            setBackgroundColor(Color.BLACK)
        }
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        addView(container, params)

        initTextureView()
        addTextureView()
    }

    private fun initTextureView() {
        _textureView ?: XTextureView(this.context).also {
            it.surfaceTextureListener = this
            it.setDisplayType(displayType)
            _textureView = it
        }
    }

    private fun addTextureView() {
        container.removeView(textureView)
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            Gravity.CENTER)
        container.addView(textureView, 0, params)
    }

    private fun openMediaPlayer() {

        _surface ?: Surface(surfaceTexture).also {
            _surface = it
        }

        //media.start("/data/local/tmp/v1080.mp4", surface)
        //media.start("rtsp://wowzaec2demo.streamlock.net/vod/mp4", surface)
        //media.start("rtsp://admin:br123456789@192.168.1.39:554/avstream", surface)
    }

    override fun onSurfaceTextureAvailable(surface2: SurfaceTexture, width: Int, height: Int) {
        if (_surfaceTexture == null) {
            _surfaceTexture = surface2
            openMediaPlayer()
        } else {
            textureView.setSurfaceTexture(surfaceTexture)
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return _surfaceTexture == null
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

    }


}