package com.brian.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brian.R
import com.brian.VedioViewActivity
import kotlinx.android.synthetic.main.fragment_main_screen.*


class MainScreenFragment : Fragment() {
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(requireActivity(), R.id.main_dash_fragment)

        GetStart.setOnClickListener {
//            var intent = Intent(context,VedioViewActivity::class.java)
//            startActivity(intent)

           navController.navigate(R.id.action_mainScreenFragment_to_videoViewFragment)
        }

        }

    }

