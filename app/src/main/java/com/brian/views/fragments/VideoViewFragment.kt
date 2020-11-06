package com.brian.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.fragment_video_view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein


class VideoViewFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    var isTraining = false
    var categoryId: Int? = null
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Utils.init.setupFullScreen(requireActivity())
        /*requireActivity().getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )*/
        var path = arguments?.getString("path")
        isTraining = arguments?.getBoolean(getString(R.string.training_videos))!!
        categoryId = arguments?.getInt(getString(R.string.id), 0)
        initializePlayer(path!!)
//        videoView.setVideoPath(path)
        showProgress(requireContext())
        /* videoView.setBackgroundColor(Color.WHITE)
         videoView.requestFocus()
         videoView.setBackgroundColor(Color.TRANSPARENT)
 */

        videoView.setOnPreparedListener { mp ->
            videoView.start()
        }

        videoView.setOnCompletionListener {
            videoView.resume()


        }
    }


    fun initializePlayer(url: String) {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        video_view.player = player
        val mediaItem: MediaItem = MediaItem.fromUri(url)
        player!!.setMediaItem(mediaItem)
        player!!.setPlayWhenReady(playWhenReady);
//        player!!.seekTo(currentWindow, playbackPosition);
        player!!.prepare();

        player?.addListener(object : Player.DefaultEventListener() {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                    }
                    Player.STATE_BUFFERING -> {
                    }
                    Player.STATE_READY -> {
                        hideProgress()
                    }
                    Player.STATE_ENDED -> {

                        if (isTraining) {

                            findNavController().navigate(
                                R.id.action_videoViewFragment_to_trainingFragment,
                                bundleOf(getString(R.string.id) to categoryId)
                            )

                        } else {
                            findNavController().navigate(R.id.action_videoViewFragment_to_homeFragment)
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
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