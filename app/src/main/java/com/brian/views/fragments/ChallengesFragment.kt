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
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.ChallengesFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.challenges.ChallengesViewModel
import com.brian.viewModels.challenges.ChallengesViewModelFactory
import com.brian.views.adapters.MyChallengesAdapter
import com.google.android.material.tabs.TabLayout
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ChallengesFragment : ScopedFragment(), KodeinAware, TabLayout.OnTabSelectedListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: ChallengesViewModelFactory by instance()
    lateinit var mBinding: ChallengesFragmentBinding
    lateinit var mViewModel: ChallengesViewModel
    private val mClickHandler = ClickHandler()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = ChallengesFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        setupObserver()
        setupRecyclers()
        setupScrollListener()
        mViewModel.getMyChallenges()
        mViewModel.getChallengesRequests()

        mBinding.tabs.setOnTabSelectedListener(this)
        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChallengesViewModel::class.java)
    }


    inner class ClickHandler : MyChallengesAdapter.onViewClick {
        override fun onAprroveClick() {
            findNavController().navigate(R.id.challenegeFragment)
        }

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    @SuppressLint("NewApi")
    override fun onTabSelected(tab: TabLayout.Tab?) {

        if (tab?.position == 0) {
            mBinding.rMyChallenges.visibility = VISIBLE
            mBinding.rChallengeRequests.visibility = GONE
        } else {
            mBinding.rMyChallenges.visibility = GONE
            mBinding.rChallengeRequests.visibility = VISIBLE
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

            myChallenges.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    mViewModel.myChallengesAdapter.setNewItems(it)
                } else {
                    if (currentPageMyChalleneges == 1) {
                        mViewModel.myChallengesAdapter.setNewItems(it)
                    }
                }

            })

            challengeRequests.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    mViewModel.challengeRequestsAdapter.setNewItems(it)
                } else {
                    if (currentPageChallenegesRequests == 1) {
                        mViewModel.myChallengesAdapter.setNewItems(it)
                    }
                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    private fun setupScrollListener() {
        mBinding.apply {


            rMyChallenges.setOnScrollChangeListener { _, _, _, _, _ ->

                if (mViewModel.myChallenges.value?.isNotEmpty()!!) {
                    val view = rMyChallenges.getChildAt(rMyChallenges.childCount - 1)
                    val diff = view.bottom - (rMyChallenges.height + rMyChallenges.scrollY)
                    val offset = mViewModel.myChallenges.value?.size
                    if (diff == 0 && offset!! % 10 == 0 && !mViewModel.myChallengesLoaded) {
                        mViewModel.getMyChallenges()
                    }
                }


            }


            rChallengeRequests.setOnScrollChangeListener { _, _, _, _, _ ->

                if (mViewModel.challengeRequests.value?.isNotEmpty()!!) {
                    val view = rChallengeRequests.getChildAt(rChallengeRequests.childCount - 1)
                    val diff =
                        view.bottom - (rChallengeRequests.height + rChallengeRequests.scrollY)
                    val offset = mViewModel.challengeRequests.value?.size
                    if (diff == 0 && offset!! % 10 == 0 && !mViewModel.challengeRequestsLoaded) {
                        mViewModel.getChallengesRequests()
                    }
                }


            }


        }
    }


    private fun setupRecyclers() {
        mBinding.apply {


            rMyChallenges.apply {
                adapter = mViewModel.myChallengesAdapter
            }

            rChallengeRequests.apply {
                adapter = mViewModel.challengeRequestsAdapter
            }


        }
    }

}