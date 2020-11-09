package com.brian.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.brian.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.activity_video_view.*


class VideoViewActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)
        initializePlayer(intent.getStringExtra(getString(R.string.training_videos)))

        if (intent.getStringExtra(getString(R.string.training_videos)).endsWith(".mp3")) {
            ivAudio.visibility = View.VISIBLE

        }

        ivBack.setOnClickListener {
            super.onBackPressed()
        }
    }


    override fun onResume() {
        super.onResume()
        hideSystemUi()
    }

    private fun initializePlayer(url: String) {
        player = SimpleExoPlayer.Builder(this).build()
        video_view.player = player
        val mediaItem: MediaItem = MediaItem.fromUri(url)
        player!!.setMediaItem(mediaItem)
        player!!.setPlayWhenReady(playWhenReady);
        player!!.seekTo(currentWindow, playbackPosition);
        player!!.prepare();
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        video_view.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        )
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }
}
