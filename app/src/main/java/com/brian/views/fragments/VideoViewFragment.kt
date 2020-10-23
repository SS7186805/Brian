package com.brian.views.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.internals.Utils
import kotlinx.android.synthetic.main.fragment_video_view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein


class VideoViewFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // Utils.init.setupFullScreen(requireActivity())
        videoView.setVideoURI(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.videoview))
        videoView.requestFocus()
        videoView.start()
        videoView.setOnCompletionListener {
            videoView.resume()
            findNavController().navigate(R.id.action_videoViewFragment_to_homeFragment)
        }
    }
}