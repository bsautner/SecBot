package com.secbot

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

/**
 * raspivid -o - -t 0 -n | cvlc -vvv stream:///dev/stdin --sout '#rtp{sdp=rtsp://:8554/}' :demux=h264
 */

class VideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

    }

    override fun onStart() {

        super.onStart()
        val uri = "rtsp://192.168.1.68:8554/"

        val v = findViewById<View>(R.id.playerView) as VideoView
        v.setVideoURI(Uri.parse(uri))
        v.setMediaController(MediaController(this))
        v.requestFocus()
        v.start()
    }

    override fun onStop() {

        super.onStop()
    }
}