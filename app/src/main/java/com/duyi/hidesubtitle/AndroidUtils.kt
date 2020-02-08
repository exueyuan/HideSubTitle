package com.duyi.hidesubtitle

import android.content.Context
import android.util.DisplayMetrics



class AndroidUtils {
    companion object {
        fun getScreeenWidth(context: Context):Int {
            val resources = context.getResources()
            val dm = resources.getDisplayMetrics()
            return dm.widthPixels
        }

        fun getScreeenHeight(context: Context):Int {
            val resources = context.getResources()
            val dm = resources.getDisplayMetrics()
            return dm.heightPixels
        }
    }
}