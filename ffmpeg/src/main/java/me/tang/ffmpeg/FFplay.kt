package me.tang.ffmpeg

import android.view.Surface

class FFplay {

    external fun open(filename: String, surface: Surface) : Long

    external fun close(handle: Long)

    external fun sendEvent(handle: Long, code: Int)

    external fun setVolume(handle: Long, volume: Long)

    external fun duration(handle: Long) : Long

    external fun currentDuration(handle: Long) : Long

    external fun seek(handle: Long, pos: Long)

    external fun setRate(handle: Long, rate: Int)

    external fun setSurface(handle: Long, surface: Surface)

    companion object {
        init {
            System.loadLibrary("ffplay")
        }

        @JvmStatic
        val FF_EVENT_TOOGLE_PAUSE = 0 //暂停
        @JvmStatic
        val FF_EVENT_TOOGLE_MUTE = 1 //静音
        @JvmStatic
        val FF_EVENT_INC_VOLUME = 2 //静音加
        @JvmStatic
        val FF_EVENT_DEC_VOLUME = 3 //静音减
        @JvmStatic
        val FF_EVENT_NEXT_FRAME = 4 //下一帧
        @JvmStatic
        val FF_EVENT_FAST_BACK = 5 //后退
        @JvmStatic
        val FF_EVENT_FAST_FORWARD = 6 //快进

        fun open(filename: String, surface: Surface) = instance.open(filename, surface)

        fun close(handle: Long) = instance.close(handle)

        fun sendEvent(handle: Long, code: Int) = instance.sendEvent(handle, code)

        fun setVolume(handle: Long, volume: Long) = instance.setVolume(handle, volume)

        fun duration(handle: Long) : Long = instance.duration(handle)

        fun currentDuration(handle: Long) : Long = instance.currentDuration(handle)

        fun seek(handle: Long, pos: Long) = instance.seek(handle, pos)

        fun setRate(handle: Long, rate: Int) = instance.setRate(handle, rate)

        fun setSurface(handle: Long, surface: Surface) = instance.setSurface(handle, surface)

        private var _instance: FFplay? = null
            get() {
                if (field != null) {
                    return field
                }
                return FFplay()
            }
        private val instance get() =  _instance!!
    }
}
