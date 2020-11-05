package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.base.EndlessRecyclerViewScrollListener
import com.brian.base.ScopedFragment
import com.brian.databinding.MyTeamsFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.brian.views.adapters.MyTeamsAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MyTeamsFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: MyTeamsFragmentBinding
    lateinit var mViewModel: HomeViewModel
    var mEndlessFriendsecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = MyTeamsFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        mBinding.toolbar.tvTitle.text = getString(R.string.my_teams)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setupObserver()
        setupRecyclers()
        setupScrollListener()
        onSwipe()
        loadData()
        mViewModel.myTeamsAdapter.listener = ClickHandler()


        return mBinding.root
    }

    fun loadData() {
        mViewModel.myTeams.value?.clear()
        mViewModel.myTeamsAdapter.clearData()
        mViewModel.currentPageTeams = 1
        mViewModel.getMyTeams()


    }

    fun onSwipe() {
        mBinding.lSwipe.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.yellow));
        mBinding.lSwipe.setOnRefreshListener {
            loadData()
            mBinding.lSwipe.isRefreshing = false

        }
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    inner class ClickHandler : MyTeamsAdapter.onClickEvents {

        override fun onTeamClick(position: Int) {
            findNavController().navigate(
                R.id.myTeamfragment,
                bundleOf(getString(R.string.team_player) to mViewModel.myTeams.value!![position].teamMembers)
            )
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

            myTeams.observe(viewLifecycleOwner, Observer {
                if (it != null && it.isNotEmpty()) {
                    mBinding.tvNobadges.visibility = View.GONE
                    mViewModel.myTeamsAdapter.setNewItems(it)
                } else {
                    if (mViewModel.myTeams.value!!.size == 0) {
                        mBinding.tvNobadges.visibility = View.VISIBLE
                    }
                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }


    private fun setupScrollListener() {

        mEndlessFriendsecyclerViewScrollListener =
            object :
                EndlessRecyclerViewScrollListener(mBinding.recycler.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.e("ONLOADMOREMyUsers", "Onloadmore" + mViewModel.myTeams.value?.size)

                    if (mViewModel.myTeams.value!!.size % 10 == 0 && mViewModel.myTeams.value!!.size != 0) {
                        mViewModel.getMyTeams()
                    }

                }
            }



        mBinding.recycler.addOnScrollListener(mEndlessFriendsecyclerViewScrollListener!!)


    }


    private fun setupRecyclers() {
        mBinding.apply {
            recycler.apply {
                adapter = mViewModel.myTeamsAdapter
            }

        }
    }


}