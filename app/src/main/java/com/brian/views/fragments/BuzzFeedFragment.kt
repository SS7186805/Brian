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
import com.brian.databinding.BuzzFeedBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.buzzFeed.BuzzFeedViewModel
import com.brian.viewModels.trainingVideos.BuzzFeedViewModelFactory
import com.brian.views.adapters.BuzzFeedAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class BuzzFeedFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: BuzzFeedViewModelFactory by instance()
    lateinit var mBinding: BuzzFeedBinding
    lateinit var mViewModel: BuzzFeedViewModel
    var mEndlessBuzzFeedViewScrollListener: EndlessRecyclerViewScrollListener? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = BuzzFeedBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }


        mViewModel.buzzFeedAdapter.listener = this.ClickHandler()
        setupObserver()
        setupRecyclers()
        setupScrollListener()

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(BuzzFeedViewModel::class.java)
    }

    inner class ClickHandler : BuzzFeedAdapter.onClickEvents {


        override fun onVideoClick(position: Int) {
            findNavController().navigate(
                R.id.buzzFeedDetailsFragment,
                bundleOf(getString(R.string.buzz_feed_details) to mViewModel.feedList.value!![position])
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

            feedList.observe(viewLifecycleOwner, Observer {
                if (it != null && it.isNotEmpty()) {
                    mBinding.tvNoDatFound.visibility = View.GONE
                    mViewModel.buzzFeedAdapter.setNewItems(it)
                } else {
                    if (mViewModel.feedList.value!!.size == 0) {
                        mBinding.tvNoDatFound.visibility = View.VISIBLE
                    }
                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    private fun setupScrollListener() {

        mEndlessBuzzFeedViewScrollListener =
            object :
                EndlessRecyclerViewScrollListener(mBinding.recyclerFeed.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.e("ONLOADMOREMyUsers", "Onloadmore" + mViewModel.feedList.value?.size)

                    if (mViewModel.feedList.value!!.size % 10 == 0 && mViewModel.feedList.value!!.size != 0) {
                        mViewModel.getBuzzFeed()
                    }

                }
            }



        mBinding.recyclerFeed.addOnScrollListener(mEndlessBuzzFeedViewScrollListener!!)


    }

    private fun setupRecyclers() {
        mBinding.apply {
            recyclerFeed.apply {
                adapter = mViewModel.buzzFeedAdapter
            }

        }
    }


}