package com.duyi.hidesubtitle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE = 1111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //打开权限
        bt_open_authority.setOnClickListener {
            startFloatingService()
        }
        sb_width.max = AndroidUtils.getScreeenWidth(this)
        sb_width.progress = FloatingService.DEFAULT_WIDTH

        sb_width.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(sb: SeekBar?, progress: Int, p2: Boolean) {
                IntentSkip.startFloatService(this@MainActivity, width = progress, height = -1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        sb_height.max = AndroidUtils.getScreeenHeight(this)
        sb_height.progress = FloatingService.DEFAULT_HEIGHT
        sb_height.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(sb: SeekBar?, progress: Int, p2: Boolean) {
                IntentSkip.startFloatService(this@MainActivity, width = -1, height = progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    //开启悬浮窗的service
    fun startFloatingService() {
        val hasAuthority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }

        if (!hasAuthority) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT)
            startActivityForResult(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                ), REQUEST_CODE
            )
        } else {
            IntentSkip.startFloatService(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            val hasAuthority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Settings.canDrawOverlays(this)
            } else {
                true
            }
            if (!hasAuthority) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show()
                IntentSkip.startFloatService(this)
            }
        }
    }
}
