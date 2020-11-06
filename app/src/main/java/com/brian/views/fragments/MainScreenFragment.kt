package com.brian.views.fragments

import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.MainApplication
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentMainScreenBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class MainScreenFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: FragmentMainScreenBinding
    lateinit var mViewModel: HomeViewModel
    var videoUrl = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = FragmentMainScreenBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }



        setupObserver()
        loadData()
        onSwipe()
        return mBinding.root
    }

    fun loadData() {
        if (videoUrl.equals("")) {
            mViewModel.getData()

        }
    }

    fun onSwipe() {
        mBinding.lSwipe.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.yellow));
        mBinding.lSwipe.setOnRefreshListener {
            loadData()
            mBinding.lSwipe.isRefreshing = false

        }
    }


    inner class ClickHandler {

        fun onStart() {

            if (MainApplication.hasNetwork()) {
                findNavController().navigate(
                    R.id.action_mainScreenFragment_to_videoViewFragment,
                    bundleOf("path" to videoUrl, getString(R.string.training_videos) to false)
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.internet_alert),
                    Toast.LENGTH_SHORT
                ).show()
            }


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

            homeData.observe(viewLifecycleOwner, Observer {
                if (it != null && it.isNotEmpty()) {

                    mBinding.textView.setText(
                        Html.fromHtml(
                            it[0].description,
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
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

}

