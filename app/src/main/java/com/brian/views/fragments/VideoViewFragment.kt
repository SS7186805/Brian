package com.brian.views.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import kotlinx.android.synthetic.main.fragment_video_view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class VideoViewFragment :ScopedFragment(), KodeinAware {
  //  lateinit var videoView: VideoView
    override val kodein by closestKodein()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoView.setVideoURI(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.videoview))

        videoView.requestFocus()

        videoView.start()

        videoView.setOnCompletionListener {
            findNavController().navigate(R.id.homeFragment)
        }

    }
}