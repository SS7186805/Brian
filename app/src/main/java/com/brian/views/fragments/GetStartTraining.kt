package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.brian.R
import kotlinx.android.synthetic.main.fragment_get_start_training.*

class GetStartTraining:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_start_training, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GetStart_one.setOnClickListener {
            findNavController().navigate(R.id.trainingVideosFragment)
        }

        GetStart_second.setOnClickListener {
            findNavController().navigate(R.id.trainingVideosFragment)
        }
    }
}