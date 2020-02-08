package com.duyi.hidesubtitle

import android.content.Context
import android.content.Intent

class IntentSkip  {
    companion object {
        fun startFloatService(context: Context, width:Int = FloatingService.DEFAULT_WIDTH, height:Int = FloatingService.DEFAULT_HEIGHT) {
            val intent = Intent(context, FloatingService::class.java)
            intent.putExtra(FloatingService.INTENT_WIDTH, width)
            intent.putExtra(FloatingService.INTENT_HEIGHT, height)
            context.startService(intent)
        }
    }


}