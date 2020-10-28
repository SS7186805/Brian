package com.brian.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brian.base.ScopedFragment
import com.brian.databinding.LeaderboardFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.google.android.material.tabs.TabLayout
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class LeaderBoardFragment : ScopedFragment(), KodeinAware, TabLayout.OnTabSelectedListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: LeaderboardFragmentBinding
    lateinit var mViewModel: HomeViewModel
    private val mClickHandler = ClickHandler()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = LeaderboardFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }


        setupObserver()
        setupRecyclers()
        mViewModel.getAllChallenges()
        mViewModel.getAllPlayers()
        mBinding.tabs.setOnTabSelectedListener(this)
        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }


    inner class ClickHandler {


    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    @SuppressLint("NewApi")
    override fun onTabSelected(tab: TabLayout.Tab?) {

        if (tab?.position == 0) {
            mBinding.rChallenges.visibility = VISIBLE
            mBinding.rPlayers.visibility = GONE
        } else {
            mBinding.rChallenges.visibility = GONE
            mBinding.rPlayers.visibility = VISIBLE
        }

    }


    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {
                    requireContext().showToast(it)
                    showMessage.postValue("")
                }
            })

            challenges.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    mViewModel.myChallengesAdapter.setNewItems(it)
                }
            })

            players.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    mViewModel.playersAdapter.setNewItems(it)
                }
            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }


    private fun setupRecyclers() {
        mBinding.apply {


            rChallenges.apply {
                adapter = mViewModel.myChallengesAdapter
            }

            rPlayers.apply {
                adapter = mViewModel.playersAdapter
            }


        }
    }

}