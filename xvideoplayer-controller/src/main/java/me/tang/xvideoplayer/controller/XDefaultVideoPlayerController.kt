package me.tang.xvideoplayer.controller

import android.content.Context
import android.util.AttributeSet
import me.tang.xvideoplayer.XVideoPlayerController

class XDefaultVideoPlayerController : XVideoPlayerController {

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {

    }

}