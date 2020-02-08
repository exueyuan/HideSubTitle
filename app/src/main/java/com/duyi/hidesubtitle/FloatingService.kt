package com.duyi.hidesubtitle

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

class FloatingService : Service() {
    companion object {
        const val INTENT_WIDTH = "intent_width"
        const val INTENT_HEIGHT = "intent_height"
        const val INTENT_START_HIDE = "intent_start_hide"

        const val DEFAULT_WIDTH = 500
        const val DEFAULT_HEIGHT = 100
        const val DEFAULT_X_PROPORTION = 0.8
        const val DEFAULT_Y_PROPORTION = 0.8
        var isStarted = false

    }

    private lateinit var windowManager: WindowManager
    private lateinit var layoutParams: WindowManager.LayoutParams

    var rootView: View? = null


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        layoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.width = DEFAULT_WIDTH
        layoutParams.height = DEFAULT_HEIGHT
        layoutParams.x = (AndroidUtils.getScreeenWidth(this) - layoutParams.width)/2
        layoutParams.y = (DEFAULT_Y_PROPORTION * AndroidUtils.getScreeenHeight(this)).toInt()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val startHide = intent?.getBooleanExtra(INTENT_START_HIDE, true)?:true
        if (startHide) {
            if (isStarted) {
                val width = intent?.getIntExtra(INTENT_WIDTH, DEFAULT_WIDTH) ?: DEFAULT_WIDTH
                val height = intent?.getIntExtra(INTENT_HEIGHT, DEFAULT_HEIGHT) ?: DEFAULT_HEIGHT
                if (width > 0) {
                    val widthCenter = layoutParams.x + layoutParams.width / 2
                    layoutParams.x = widthCenter - width / 2
                    layoutParams.width = width
                }
                if (height > 0) {
                    layoutParams.height = height
                }

                windowManager.updateViewLayout(rootView, layoutParams)
            } else {
                showFloatingWindow()
            }
        } else {
            hideFloatingWindow()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showFloatingWindow() {
        isStarted = true
        rootView = View(this)
        rootView?.setBackgroundColor(Color.BLUE)
        windowManager.addView(rootView, layoutParams)

        rootView?.setOnTouchListener(FloatingOnTouchListener())
    }

    private fun hideFloatingWindow() {
        isStarted = false
        windowManager.removeView(rootView)
    }

    //设置触摸listenner
    private inner class FloatingOnTouchListener : View.OnTouchListener {
        private var x: Int = 0
        private var y: Int = 0

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.rawX.toInt()
                    y = event.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = event.rawX.toInt()
                    val nowY = event.rawY.toInt()
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY
                    layoutParams.x = layoutParams.x + movedX
                    layoutParams.y = layoutParams.y + movedY
                    windowManager.updateViewLayout(view, layoutParams)
                }
                else -> {
                }
            }
            return true
        }
    }



}