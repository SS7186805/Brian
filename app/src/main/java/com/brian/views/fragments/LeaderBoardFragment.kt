package com.brian.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brian.base.EndlessRecyclerViewScrollListener
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
    var mEndlessLeaderBoardViewScrollListener: EndlessRecyclerViewScrollListener? = null
    var mEndlessPlayersBoardViewScrollListener: EndlessRecyclerViewScrollListener? = null


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
        setupScrollListener()
        mBinding.tabs.setOnTabSelectedListener(this)
        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }


    private fun setupScrollListener() {

        mEndlessLeaderBoardViewScrollListener =
            object :
                EndlessRecyclerViewScrollListener(mBinding.rChallenges.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.e("ONLOADMOREMyUsers", "Onloadmore" + mViewModel.challenges.value?.size)

                    if (mViewModel.challenges.value!!.size % 10 == 0 && mViewModel.challenges.value!!.size != 0) {
                        mViewModel.getAllChallenges()
                    }

                }
            }

        mEndlessPlayersBoardViewScrollListener =
            object :
                EndlessRecyclerViewScrollListener(mBinding.rPlayers.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.e("ONLOADMOREMyUsers", "Onloadmore" + mViewModel.players.value?.size)

                    if (mViewModel.players.value!!.size % 10 == 0 && mViewModel.players.value!!.size != 0) {
                        mViewModel.getAllPlayers()
                    }

                }
            }




        mBinding.rChallenges.addOnScrollListener(mEndlessLeaderBoardViewScrollListener!!)
        mBinding.rPlayers.addOnScrollListener(mEndlessPlayersBoardViewScrollListener!!)


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
            mBinding.tvNoDatPlayers.visibility = GONE
            if (mViewModel.challenges.value?.isEmpty()!!) {
                mBinding.tvNoDataChallenges.visibility = VISIBLE
            } else {
                mBinding.tvNoDataChallenges.visibility = GONE

            }
            mBinding.rChallenges.visibility = VISIBLE
            mBinding.rPlayers.visibility = GONE
        } else {
            mBinding.tvNoDataChallenges.visibility = GONE
            if (mViewModel.players.value?.isEmpty()!!) {
                mBinding.tvNoDatPlayers.visibility = VISIBLE
            } else {
                mBinding.tvNoDatPlayers.visibility = GONE

            }
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
                if (it != null && it.isNotEmpty()) {
                    mBinding.tvNoDataChallenges.visibility = View.GONE
                    mBinding.tvNoDatPlayers.visibility = View.GONE
                    mViewModel.myChallengesAdapter.setNewItems(it)
                } else {
                    if (mViewModel.challenges.value!!.size == 0) {
                        mBinding.tvNoDataChallenges.visibility = View.VISIBLE
                    }
                }
            })

            players.observe(viewLifecycleOwner, Observer {
                if (it != null && it.isNotEmpty()) {
                    mBinding.tvNoDataChallenges.visibility = View.GONE
                    mBinding.tvNoDatPlayers.visibility = View.GONE
                    mViewModel.playersAdapter.setNewItems(it)
                } else {
                    if (mViewModel.players.value!!.size == 0) {
                        mBinding.tvNoDatPlayers.visibility = View.VISIBLE
                    }
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