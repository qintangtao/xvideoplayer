package me.tang.xvideoplayer

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils

class XVideoPlayer : FrameLayout {

    companion object {
        @JvmStatic
        val TAG = XVideoPlayer::class.java.simpleName

        @JvmStatic
        val MODE_NORMAL = 10
        @JvmStatic
        val MODE_FULL_SCREEN = 11
        @JvmStatic
        val MODE_TINY_WINDOW = 12

    }

    //
    private var _currentMode = MODE_NORMAL
    val currentMode get() = _currentMode

    //view
    private lateinit var _container: FrameLayout
    private val container get() = _container

    private var _media: XVideoPlayerMedia? = null
    private val media get() = _media!!

    private var _controler: XVideoPlayerController? = null
    private val controller get() = _controler!!

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet?) {
        _container = FrameLayout(context).apply {
            setBackgroundColor(Color.BLACK)
        }
        val params = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        addView(container, params)
    }

    fun setMedia(media: XVideoPlayerMedia) {
        _media = media
    }

    fun setController(controller2: XVideoPlayerController) {
        _controler?.let {
            container.removeView(it)
        }
        _controler = controller2
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        container.addView(controller, params)
    }

    fun enterFullScreen() {
        if (currentMode == MODE_FULL_SCREEN)
            return

        XUtil.hideActionBar(this.context)
        XUtil.setOrientation(this.context, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        val contentView = XUtil.scanForActivity(this.context)?.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        check(contentView != null) { "ID_ANDROID_CONTENT not found" }

        if (currentMode == MODE_TINY_WINDOW)
            contentView.removeView(container)
        else
            removeView(container)

        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        contentView.addView(container, params)

        _currentMode = MODE_FULL_SCREEN
    }

    fun exitFullScreen(): Boolean{
        if (currentMode != MODE_FULL_SCREEN)
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

        _currentMode = MODE_NORMAL
        return true
    }

    fun enterTinyWindow() {
        if (currentMode == MODE_TINY_WINDOW)
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

        _currentMode = MODE_TINY_WINDOW
    }

    fun exitTinyWindow(): Boolean{
        if (currentMode != MODE_TINY_WINDOW)
            return false

        val contentView = XUtil.scanForActivity(this.context)?.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        check(contentView != null) { "ID_ANDROID_CONTENT not found" }

        contentView.removeView(container)

        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        addView(container, params)

        _currentMode = MODE_NORMAL
        return true
    }


}