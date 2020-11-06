package com.brian.views.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.base.EndlessRecyclerViewScrollListener
import com.brian.base.Prefs
import com.brian.base.ScopedFragment
import com.brian.databinding.ChallengesFragmentBinding
import com.brian.internals.DialogUtil
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.challenges.ChallengesViewModel
import com.brian.viewModels.challenges.ChallengesViewModelFactory
import com.brian.views.activities.VideoViewActivity
import com.brian.views.adapters.MyChallengesAdapter
import com.brian.views.adapters.MyChallengesRequestsAdapter
import com.google.android.material.tabs.TabLayout
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ChallengesFragment : ScopedFragment(), KodeinAware, TabLayout.OnTabSelectedListener,
    DialogUtil.YesNoDialogClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: ChallengesViewModelFactory by instance()
    lateinit var mBinding: ChallengesFragmentBinding
    lateinit var mViewModel: ChallengesViewModel
    private val mClickHandler = ClickHandler()
    private lateinit var myChallengeslinearLayoutManager: LinearLayoutManager
    private lateinit var challengeRequestslinearLayoutManager: LinearLayoutManager
    var mEndlessMyChallengesRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    var mEndlessChallengeRequestsViewScrollListener: EndlessRecyclerViewScrollListener? = null


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
        onSwipe()

        mViewModel.challengeRequestsAdapter.listener = this.mClickHandler
        mViewModel.myChallengesAdapter.listener = this.mClickHandler



        mBinding.tabs.setOnTabSelectedListener(this)
        return mBinding.root
    }


    fun loadData() {
        mViewModel.myChallenges.value?.clear()
        mViewModel.challengeRequests.value?.clear()
        mViewModel.myChallengesAdapter.clearData()
        mViewModel.challengeRequestsAdapter.clearData()
        mViewModel.currentPageAllChalleneges = 1
        mViewModel.currentPageMyChalleneges = 1
        mViewModel.currentPageChallenegesRequests = 1
        mViewModel.getMyChallenges()
        mViewModel.getChallengesRequests()

    }

    fun onSwipe() {
        mBinding.lSwipe.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.yellow));

        mBinding.lSwipe.setOnRefreshListener {
            Log.e("OnSwipee", "OnSwipee")
            loadData()
            mBinding.lSwipe.isRefreshing = false

        }
    }


    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChallengesViewModel::class.java)
    }


    inner class ClickHandler : MyChallengesAdapter.onViewClick,
        MyChallengesRequestsAdapter.onViewClick {

        override fun onRequestAcceptClick(position: Int) {
            findNavController().navigate(
                R.id.challenegeFragment,
                bundleOf(getString(R.string.challenge) to mViewModel.challengeRequests.value!![position])
            )
        }

        override fun onRequestRejectClick(position: Int) {
            mViewModel.acceptChallengeRequestParams.user_challenge_id =
                mViewModel.challengeRequests.value!![position].id
            mViewModel.acceptChallengeRequestParams.status = 2
            mViewModel.acceptChallengeRequests()
        }

        override fun onCancelChallenge(position: Int) {
            println("mdfndkjfnjkdf")
            Log.e("MbbjjUserIdkkk", "IDDD${Prefs.init().userInfo?.id.toString()}")

            DialogUtil.build(requireContext()) {
                title = getString(R.string.error)
                dialogType = DialogUtil.DialogType.YES_NO_TYPE
                message = getString(R.string.cancel_challenge_message)
                yesNoDialogClickListener = this@ChallengesFragment
            }



            mViewModel.rejectChallengeRequestParams.user_challenge_id =
                mViewModel.myChallenges.value!![position].id

        }

        override fun onApproveRejectChallenge(position: Int, status: Int) {
            mViewModel.approveRejectMyChallengeRequestParams.user_challenge_id =
                mViewModel.myChallenges.value!![position].id
            mViewModel.approveRejectMyChallengeRequestParams.status = status
            mViewModel.approveRejectChallengeRequests()
        }

        override fun onVideoClick(position: Int, url: String) {
            startActivity(
                Intent(requireContext(), VideoViewActivity::class.java).putExtra(
                    getString(R.string.training_videos),
                    url
                )
            )
        }

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    @SuppressLint("NewApi")
    override fun onTabSelected(tab: TabLayout.Tab?) {

        if (tab?.position == 0) {
            mBinding.tvNoDataFound.visibility = GONE
            if (mViewModel.myChallenges.value?.isEmpty()!!) {
                mBinding.tvNoMyChalleneges.visibility = VISIBLE
            } else {
                mBinding.tvNoMyChalleneges.visibility = GONE

            }
            mBinding.rMyChallenges.visibility = VISIBLE
            mBinding.rChallengeRequests.visibility = GONE
        } else {

            mBinding.tvNoMyChalleneges.visibility = GONE
            if (mViewModel.challengeRequests.value?.isEmpty()!!) {
                mBinding.tvNoDataFound.visibility = VISIBLE
            } else {
                mBinding.tvNoDataFound.visibility = GONE

            }
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

                Log.e("MyChallengesSize", myChallenges.value?.size.toString())

                if (it != null && it.isNotEmpty()) {
                    mBinding.tvNoMyChalleneges.visibility = View.GONE
                    mViewModel.myChallengesAdapter.setNewItems(it)
                } else {
                    if (mViewModel.myChallenges.value!!.size == 0) {
                        mBinding.tvNoMyChalleneges.visibility = View.VISIBLE
                        mBinding.tvNoDataFound.visibility = View.GONE

                    }
                }


            })

            cancelResponse.observe(viewLifecycleOwner, Observer {

                if (it.result?.contains(getString(R.string.success))!!) {
                    mViewModel.myChallenges.value?.clear()
                    mViewModel.myChallengesAdapter.clearData()
                    mViewModel.currentPageAllChalleneges = 1
                    mViewModel.getMyChallenges()
                }


            })
            approveRejectResponse.observe(viewLifecycleOwner, Observer {

                if (it.result?.contains(getString(R.string.success))!!) {
                    mViewModel.myChallenges.value?.clear()
                    mViewModel.myChallengesAdapter.clearData()
                    mViewModel.currentPageAllChalleneges = 1
                    mViewModel.getMyChallenges()
                }


            })
            rejectRequestResponse.observe(viewLifecycleOwner, Observer {

                if (it.result?.contains(getString(R.string.success))!!) {
                    mViewModel.challengeRequests.value?.clear()
                    mViewModel.challengeRequestsAdapter.clearData()
                    mViewModel.currentPageChallenegesRequests = 1
                    mViewModel.getChallengesRequests()
                }


            })



            challengeRequests.observe(viewLifecycleOwner, Observer {
                if (it != null && it.isNotEmpty()) {

                    Log.e("ChallnegeRequestss", "Requests")
                    mBinding.tvNoDataFound.visibility = View.GONE
                    mViewModel.challengeRequestsAdapter.setNewItems(it)
                } else {
                    if (mViewModel.challengeRequests.value!!.size == 0) {
                        if (!mBinding.rMyChallenges.isVisible) {
                            mBinding.tvNoDataFound.visibility = View.VISIBLE
                            mBinding.tvNoMyChalleneges.visibility = View.GONE


                        } else {
                            mBinding.tvNoDataFound.visibility = View.GONE

                        }
                    }
                }


            })

            showLoading.observe(viewLifecycleOwner, Observer {

                Log.e("ShowLaodingg", it.toString())
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    private fun setupScrollListener() {
        mEndlessMyChallengesRecyclerViewScrollListener =
            object :
                EndlessRecyclerViewScrollListener(mBinding.rMyChallenges.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.e("ONLOADMORE", "Onloadmore" + mViewModel.myChallenges.value?.size)

                    if (mViewModel.myChallenges.value!!.size % 10 == 0 && mViewModel.myChallenges.value!!.size != 0) {
                        mViewModel.getMyChallenges()
                    }

                }
            }


        mEndlessChallengeRequestsViewScrollListener =
            object :
                EndlessRecyclerViewScrollListener(mBinding.rMyChallenges.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.e("ONLOADMORE", "Onloadmore" + mViewModel.challengeRequests.value!!.size)

                    if (mViewModel.challengeRequests.value!!.size % 10 == 0 && mViewModel.challengeRequests.value!!.size != 0) {
                        mViewModel.getChallengesRequests()

                    }


                }
            }

        mBinding.rMyChallenges.addOnScrollListener(mEndlessMyChallengesRecyclerViewScrollListener!!)
        mBinding.rChallengeRequests.addOnScrollListener(
            mEndlessMyChallengesRecyclerViewScrollListener!!
        )

    }


    private fun setupRecyclers() {
        mBinding.apply {


            rMyChallenges.apply {
                adapter = mViewModel.myChallengesAdapter
                myChallengeslinearLayoutManager = LinearLayoutManager(context)
                layoutManager = myChallengeslinearLayoutManager
            }

            rChallengeRequests.apply {
                adapter = mViewModel.challengeRequestsAdapter
                challengeRequestslinearLayoutManager = LinearLayoutManager(context)
                layoutManager = challengeRequestslinearLayoutManager
            }


        }
    }

    override fun onClickYes() {
        mViewModel.cancelMyChallengeRequests()
    }

    override fun onClickNo() {
    }


    override fun onPause() {
        super.onPause()
        mViewModel.myChallenges.value?.clear()
        mViewModel.challengeRequests.value?.clear()
        mEndlessChallengeRequestsViewScrollListener?.resetState()
        mEndlessMyChallengesRecyclerViewScrollListener?.resetState()
    }

}