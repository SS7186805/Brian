package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentHomeBinding
import com.brian.databinding.FragmentLoginBinding
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class HomeFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: FragmentHomeBinding
    lateinit var mViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        mViewModel.getDefensive()

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    inner class ClickHandler{

        fun onPitcherClick(){
            findNavController().navigate(R.id.pitcherFragment, bundleOf("list" to mViewModel.list ))
        }

    }
}