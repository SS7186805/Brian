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
import com.brian.databinding.BuzzFeedBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.trainingVideos.BuzzFeedViewModel
import com.brian.viewModels.trainingVideos.BuzzFeedViewModelFactory
import com.brian.views.adapters.BuzzFeedAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.text.FieldPosition

class BuzzFeedFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: BuzzFeedViewModelFactory by instance()
    lateinit var mBinding: BuzzFeedBinding
    lateinit var mViewModel: BuzzFeedViewModel


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
            findNavController().navigate(R.id.buzzFeedDetailsFragment, bundleOf(getString(R.string.buzz_feed_details) to mViewModel.feedList.value!![position]))
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
                if (it.isNotEmpty()) {
                    mViewModel.buzzFeedAdapter.addNewItems(it)
                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    private fun setupScrollListener() {
        mBinding.apply {
            recyclerFeed.setOnScrollChangeListener { _, _, _, _, _ ->
                val view = recyclerFeed.getChildAt(recyclerFeed.childCount - 1)
                val diff = view.bottom - (recyclerFeed.height + recyclerFeed.scrollY)
                val offset = mViewModel.feedList.value?.size
                if (diff == 0 && offset!! % 10 == 0 && !mViewModel.allVideosLoaded) {
                    mViewModel.getBuzzFeed()
                }
            }

        }
    }


    private fun setupRecyclers() {
        mBinding.apply {
            recyclerFeed.apply {
                adapter = mViewModel.buzzFeedAdapter
            }

        }
    }


}