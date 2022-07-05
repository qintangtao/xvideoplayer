package me.tang.xvideoplayer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ScreenUtils

object XUtil {

    fun scanForActivity(context: Context): Activity? {
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return scanForActivity(context.baseContext)
        }
        return null
    }

    private fun getAppCompActivity(context: Context): AppCompatActivity? {
        if (context is AppCompatActivity) {
            return context as AppCompatActivity
        } else if (context is ContextThemeWrapper) {
            return getAppCompActivity((context as ContextThemeWrapper).getBaseContext())
        }
        return null
    }

    fun setOrientation(context: Context, orientation: Int) {
        scanForActivity(context)?.requestedOrientation = orientation
    }

    @SuppressLint("RestrictedApi")
    fun showActionBar(context: Context) {
        val ab: ActionBar? =
            getAppCompActivity(context)?.getSupportActionBar()
        ab?.run {
            setShowHideAnimationEnabled(false)
            show()
        }
        scanForActivity(context)?.window?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                insetsController?.show(WindowInsets.Type.statusBars())
            else
                clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    @SuppressLint("RestrictedApi")
    fun hideActionBar(context: Context) {
        val ab: ActionBar? =
            getAppCompActivity(context)?.getSupportActionBar()
        ab?.run {
            setShowHideAnimationEnabled(false)
            hide()
        }
        scanForActivity(context)?.window?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                insetsController?.hide(WindowInsets.Type.statusBars())
            else
                setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}