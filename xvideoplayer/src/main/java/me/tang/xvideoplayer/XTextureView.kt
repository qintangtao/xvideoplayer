package me.tang.xvideoplayer

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import android.view.View

class XTextureView : TextureView {

    companion object {
        const val DISPLAY_TYPE_ADAPTER = 0
        const val DISPLAY_TYPE_FILL_PARENT = 1
        const val DISPLAY_TYPE_FILL_SCROP = 2
        const val DISPLAY_TYPE_ORIGINAL = 3
    }

    private var videoHeight = 0
    private var videoWidth = 0
    private var displayType = XVideoPlayer.DISPLAY_TYPE_ADAPTER
    private var scale = 1.0f

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        //init(context, attrs)
    }

    fun setScale(scale: Float) {
        if (this.scale != scale) {
            this.scale = scale
            requestLayout()
        }
    }

    fun setDisplayType(type: Int) {
        if (displayType != type) {
            Log.d(XVideoPlayer.TAG, "displayType: $type")
            displayType = type
            requestLayout()
        }
    }

    fun setVideoSize(width: Int, height: Int) {
        if (videoWidth != width && videoHeight != height) {
            videoWidth = width
            videoHeight = height
            requestLayout()
        }
    }

    override fun setRotation(rotation: Float) {
        if (rotation != getRotation()) {
            super.setRotation(rotation)
            requestLayout()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var _widthMeasureSpec = widthMeasureSpec
        var _heightMeasureSpec = heightMeasureSpec

        var _videoWidth = videoWidth
        var _videoHeight = videoHeight

        var parentWidth = (parent as View).measuredWidth
        var parentHeight = (parent as View).measuredHeight
        if (parentWidth != 0 && parentHeight != 0 && _videoWidth != 0 && _videoHeight != 0) {
            if (displayType == DISPLAY_TYPE_FILL_PARENT) {
                if (rotation == 90f || rotation == 270f) {
                    val tempWidth = parentWidth
                    parentWidth = parentHeight
                    parentHeight = tempWidth
                }
                _videoHeight = _videoWidth * parentHeight / parentWidth
            }
        }

        if (rotation == 90f || rotation == 270f) {
            _widthMeasureSpec = heightMeasureSpec
            _heightMeasureSpec = widthMeasureSpec
        }

        var width = getDefaultSize(_videoWidth, _widthMeasureSpec)
        var height = getDefaultSize(_videoHeight, _heightMeasureSpec)
        if (_videoWidth > 0 && _videoWidth > 0) {

            val widthSpecMode = MeasureSpec.getMode(_widthMeasureSpec)
            val widthSpecSize = MeasureSpec.getSize(_widthMeasureSpec)
            val heightSpecMode = MeasureSpec.getMode(_heightMeasureSpec)
            val heightSpecSize = MeasureSpec.getSize(_heightMeasureSpec)

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize
                height = heightSpecSize
                // for compatibility, we adjust size based on aspect ratio
                if (_videoWidth * height < width * _videoHeight) {
                    width = height * _videoWidth / _videoHeight
                } else if (_videoWidth * height > width * _videoHeight) {
                    height = width * _videoHeight / _videoWidth
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize
                height = width * _videoHeight / _videoWidth
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize
                    width = height * _videoWidth / _videoHeight
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize
                width = height * _videoWidth / _videoHeight
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize
                    height = width * _videoHeight / _videoWidth
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = _videoWidth
                height = _videoHeight
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize
                    width = height * _videoWidth / _videoHeight
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize
                    height = width * _videoHeight / _videoWidth
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }

        if (parentWidth != 0 && parentHeight != 0 && _videoWidth != 0 && _videoHeight != 0) {
            if (displayType == DISPLAY_TYPE_ORIGINAL) {
                width = _videoWidth
                height = _videoHeight
            } else if (displayType == DISPLAY_TYPE_FILL_SCROP) {
                if (rotation == 90f || rotation == 270f) {
                    val tempWidth = parentWidth
                    parentWidth = parentHeight
                    parentHeight = tempWidth
                }
                if (_videoHeight / _videoWidth > parentHeight / parentWidth) {
                    width = parentWidth
                    height = parentWidth / width * height
                } else if (_videoHeight / _videoWidth < parentHeight / parentWidth) {
                    width = parentHeight / height * width
                    height = parentHeight
                }
            }
        }

        setMeasuredDimension((width*scale).toInt(), (height*scale).toInt())
    }
}