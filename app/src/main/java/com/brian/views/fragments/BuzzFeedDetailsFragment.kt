package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.BuzzFeedDetailsFragmentBinding
import com.brian.models.BuzzFeedDataItem
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.adapters.SlideViewRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class BuzzFeedDetailsFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: BuzzFeedDetailsFragmentBinding
    lateinit var mViewModel: RegisterViewModel
    var buzzFeedDetails = BuzzFeedDataItem()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = BuzzFeedDetailsFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = getString(R.string.buzz_feed_details)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buzzFeedDetails =
            arguments?.getParcelable<BuzzFeedDataItem>(getString(R.string.buzz_feed_details))!!
        mBinding.item = buzzFeedDetails

        val images = buzzFeedDetails.buzzFeedFiles
        mBinding.svSafariImages2.setSliderAdapter(SlideViewRecyclerAdapter(images!!))

    }

    inner class ClickHandler {

        fun onPitchClick() {
            findNavController().navigate(R.id.questionsFragment)

        }

    }
}