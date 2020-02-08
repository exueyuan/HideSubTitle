package com.duyi.hidesubtitle

import android.content.Context
import android.content.Intent

class IntentSkip  {
    companion object {
        fun startFloatService(context: Context,startOrHide:Boolean = true, width:Int = FloatingService.DEFAULT_WIDTH, height:Int = FloatingService.DEFAULT_HEIGHT) {
            val intent = Intent(context, FloatingService::class.java)
            intent.putExtra(FloatingService.INTENT_WIDTH, width)
            intent.putExtra(FloatingService.INTENT_HEIGHT, height)
            intent.putExtra(FloatingService.INTENT_START_HIDE, startOrHide)
            context.startService(intent)
        }
    }


}