package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.TeamsFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.brian.views.adapters.MyTeamsAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class TeamsFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: TeamsFragmentBinding
    lateinit var mViewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = TeamsFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        setupObserver()
        setupRecyclers()
        setupScrollListener()
        mViewModel.getMyTeams()
        mViewModel.myTeamsAdapter.listener=ClickHandler()


        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    inner class ClickHandler :MyTeamsAdapter.onClickEvents{

        override fun onTeamClick(position: Int) {
            findNavController().navigate(R.id.myTeamfragment, bundleOf(getString(R.string.team_player) to mViewModel.myTeams.value!![position].teamMembers))
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
                if (it.isNotEmpty()) {
                    mViewModel.myTeamsAdapter.setNewItems(it)
                }

                if(myTeams.value.isNullOrEmpty()){
                    mBinding.tvNobadges.visibility=View.VISIBLE
                }else{
                    mBinding.tvNobadges.visibility=View.GONE
                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    private fun setupScrollListener() {
        mBinding.apply {
            recycler.setOnScrollChangeListener { _, _, _, _, _ ->
                val view = recycler.getChildAt(recycler.childCount - 1)
                val diff = view.bottom - (recycler.height + recycler.scrollY)
                val offset = mViewModel.myTeams.value?.size
                if (diff == 0 && offset!! % 10 == 0 && !mViewModel.allTeamsLoaded) {
                    mViewModel.getMyTeams()
                }
            }

        }
    }


    private fun setupRecyclers() {
        mBinding.apply {
            recycler.apply {
                adapter = mViewModel.myTeamsAdapter
            }

        }
    }


}