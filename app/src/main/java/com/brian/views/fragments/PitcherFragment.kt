package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.PitcherFragmentBinding
import com.brian.internals.ClickGuard
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import kotlinx.android.synthetic.main.pitcher_fragment.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class PitcherFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: PitcherFragmentBinding
    lateinit var mViewModel: HomeViewModel
    var nextPageName = ""
    var id = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = PitcherFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
//        mBinding.toolbar.tvTitle.text = getString(R.string.situation_name)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        mViewModel.submitAnswerParams.defensive_situation_id =
            arguments?.getInt(getString(R.string.id), 0)
        mViewModel.getSelectDefensive()

        setupClickListeners()

        return mBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        id = arguments?.getInt(getString(R.string.id), 0).toString()
        var name = arguments?.getString(getString(R.string.name))
        var description = arguments?.getString(getString(R.string.description))

        SetUpScreen(name!!)
        tv_data.text = description

    }

    private fun SetUpScreen(name: String) {
        nextPageName = name
        when (name) {
            getString(R.string.pitcher) -> mBinding.toolbar.tvTitle.text =
                getString(R.string.pitcher)
            getString(R.string.catcher) -> mBinding.toolbar.tvTitle.text =
                getString(R.string.catcher)
            getString(R.string.first_base) -> mBinding.toolbar.tvTitle.text =
                getString(R.string.first_base)
            getString(R.string.second_base) -> mBinding.toolbar.tvTitle.text =
                getString(R.string.second_base)
            getString(R.string.third_base) -> mBinding.toolbar.tvTitle.text =
                getString(R.string.third_base)
            getString(R.string.short_step) -> mBinding.toolbar.tvTitle.text =
                getString(R.string.short_step)
            getString(R.string.left_field) -> mBinding.toolbar.tvTitle.text =
                getString(R.string.left_field)
            getString(R.string.center_field) -> mBinding.toolbar.tvTitle.text =
                getString(R.string.center_field)
            getString(R.string.right_field) -> mBinding.toolbar.tvTitle.text =
                getString(R.string.right_field)

            else -> ""

        }
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun setupClickListeners() {

        mBinding.apply {
            pitcher.setOnClickListener { clickHandler!!.onPitchClick() }

            ClickGuard.guard(pitcher)
        }


    }

    inner class ClickHandler {

        fun onPitchClick() {
            findNavController().navigate(
                R.id.questionsFragment,
                bundleOf("name" to nextPageName, getString(R.string.id) to id,getString(R.string.defensive_situation) to mViewModel.selectDefensiveId)
            )

        }

    }
}