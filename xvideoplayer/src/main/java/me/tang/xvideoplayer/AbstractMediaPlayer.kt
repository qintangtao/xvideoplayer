package me.tang.xvideoplayer

abstract class AbstractMediaPlayer : IMediaPlayer {

    private var mOnPreparedListener: IMediaPlayer.OnPreparedListener? = null
    private var mOnCompletionListener: IMediaPlayer.OnCompletionListener? = null
    private var mOnBufferingUpdateListener: IMediaPlayer.OnBufferingUpdateListener? = null
    private var mOnSeekCompleteListener: IMediaPlayer.OnSeekCompleteListener? = null
    private var mOnVideoSizeChangedListener: IMediaPlayer.OnVideoSizeChangedListener? = null
    private var mOnErrorListener: IMediaPlayer.OnErrorListener? = null
    private var mOnInfoListener: IMediaPlayer.OnInfoListener? = null
    private var mOnTimedTextListener: IMediaPlayer.OnTimedTextListener? = null

    override fun setOnPreparedListener(listener: IMediaPlayer.OnPreparedListener?) {
        mOnPreparedListener = listener
    }

    override fun setOnCompletionListener(listener: IMediaPlayer.OnCompletionListener?) {
        mOnCompletionListener = listener
    }

    override fun setOnBufferingUpdateListener(
        listener: IMediaPlayer.OnBufferingUpdateListener?
    ) {
        mOnBufferingUpdateListener = listener
    }

    override fun setOnSeekCompleteListener(listener: IMediaPlayer.OnSeekCompleteListener?) {
        mOnSeekCompleteListener = listener
    }

    override fun setOnVideoSizeChangedListener(
        listener: IMediaPlayer.OnVideoSizeChangedListener?
    ) {
        mOnVideoSizeChangedListener = listener
    }

    override fun setOnErrorListener(listener: IMediaPlayer.OnErrorListener?) {
        mOnErrorListener = listener
    }

    override fun setOnInfoListener(listener: IMediaPlayer.OnInfoListener?) {
        mOnInfoListener = listener
    }

    override fun setOnTimedTextListener(listener: IMediaPlayer.OnTimedTextListener?) {
        mOnTimedTextListener = listener
    }

    fun resetListeners() {
        mOnPreparedListener = null
        mOnBufferingUpdateListener = null
        mOnCompletionListener = null
        mOnSeekCompleteListener = null
        mOnVideoSizeChangedListener = null
        mOnErrorListener = null
        mOnInfoListener = null
        mOnTimedTextListener = null
    }

    protected fun notifyOnPrepared() {
        mOnPreparedListener?.onPrepared(this)
    }

    protected fun notifyOnCompletion() {
        mOnCompletionListener?.onCompletion(this)
    }

    protected fun notifyOnBufferingUpdate(percent: Int) {
        mOnBufferingUpdateListener?.onBufferingUpdate(this, percent)
    }

    protected fun notifyOnSeekComplete() {
        mOnSeekCompleteListener?.onSeekComplete(this)
    }

    protected fun notifyOnVideoSizeChanged(
        width: Int, height: Int,
        sarNum: Int, sarDen: Int
    ) {
        mOnVideoSizeChangedListener?.onVideoSizeChanged(this, width, height, sarNum, sarDen)
    }

    protected fun notifyOnError(what: Int, extra: Int): Boolean {
        return mOnErrorListener?.onError(this, what, extra) ?: false
    }

    protected fun notifyOnInfo(what: Int, extra: Int): Boolean {
        return mOnInfoListener?.onInfo(this, what, extra) ?: false
    }

    protected fun notifyOnTimedText(text: XTimedText?) {
        mOnTimedTextListener?.onTimedText(this, text)
    }

}