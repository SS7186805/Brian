package com.brian.views.fragments

import android.os.Bundle
import android.text.Html
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
import com.brian.databinding.FragmentGetStartTrainingBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.trainingVideos.TrainingViewModel
import com.brian.viewModels.trainingVideos.TrainingsViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class GetStartTraining : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: TrainingsViewModelFactory by instance()
    lateinit var mBinding: FragmentGetStartTrainingBinding
    lateinit var mViewModel: TrainingViewModel
    var videoUrl = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = FragmentGetStartTrainingBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        setupObserver()

        mViewModel.getTrainingVideoDataManagement()
        return mBinding.root
    }


    inner class ClickHandler {

        fun onStartOne() {
            findNavController().navigate(
                R.id.getStartTrainingFragment_to_videoViewFragment,
                bundleOf(
                    "path" to videoUrl,
                    getString(R.string.training_videos) to true,
                    getString(R.string.id) to 1
                )
            )

        }

        fun onStartTwo() {
            findNavController().navigate(
                R.id.getStartTrainingFragment_to_videoViewFragment,
                bundleOf(
                    "path" to videoUrl,
                    getString(R.string.training_videos) to true,
                    getString(R.string.id) to 2
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

            data.observe(viewLifecycleOwner, Observer {
                if (it != null && it.isNotEmpty()) {

                    mBinding.interactTxt.setText(
                        Html.fromHtml(
                            it[0].heading,
                            Html.FROM_HTML_MODE_COMPACT
                        )
                    )
                    mBinding.textOne.setText(
                        Html.fromHtml(
                            it[0].description1,
                            Html.FROM_HTML_MODE_COMPACT
                        )
                    )
                    mBinding.textTwo.setText(
                        Html.fromHtml(
                            it[0].description2,
                            Html.FROM_HTML_MODE_COMPACT
                        )
                    )

                    videoUrl = it[0].fileName.toString()


                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }


    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(TrainingViewModel::class.java)
    }


}