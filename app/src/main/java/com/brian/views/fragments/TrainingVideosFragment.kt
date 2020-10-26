package com.brian.views.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.TrainingVideosBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.trainingVideos.TrainingViewModel
import com.brian.viewModels.trainingVideos.TrainingsViewModelFactory
import com.brian.views.activities.VideoViewActivity
import com.brian.views.adapters.TrainingVideosAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class TrainingVideosFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: TrainingsViewModelFactory by instance()
    lateinit var mBinding: TrainingVideosBinding
    lateinit var mViewModel: TrainingViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = TrainingVideosBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        mBinding.toolbar.tvTitle.setText(getString(R.string.training_videos))

        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigate(R.id.getStartTrainingFragment)
        }

        setupObserver()
        setupScrollListener()
        setupRecyclers()
        mViewModel.trainingVideosAdapter.listener = this.ClickHandler()

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(TrainingViewModel::class.java)
    }

    inner class ClickHandler : TrainingVideosAdapter.onClickEvents {
        override fun onVideoClick(position: Int) {
            startActivity(
                Intent(requireContext(), VideoViewActivity::class.java).putExtra(
                    getString(R.string.training_videos),
                    mViewModel.videoslist.value!![position].videoUrl
                )
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

            videoslist.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    mViewModel.trainingVideosAdapter.addNewItems(it)
                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    private fun setupScrollListener() {
        mBinding.apply {
            recyclerVideos.setOnScrollChangeListener { _, _, _, _, _ ->
                val view = recyclerVideos.getChildAt(recyclerVideos.childCount - 1)
                val diff = view.bottom - (recyclerVideos.height + recyclerVideos.scrollY)
                val offset = mViewModel.videoslist.value?.size
                if (diff == 0 && offset!! % 10 == 0 && !mViewModel.allVideosLoaded) {
                    mViewModel.getVideos()
                }
            }

        }
    }


    private fun setupRecyclers() {
        mBinding.apply {
            recyclerVideos.apply {
                adapter = mViewModel.trainingVideosAdapter
            }

        }
    }


}