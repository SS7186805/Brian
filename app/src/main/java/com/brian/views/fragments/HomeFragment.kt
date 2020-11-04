package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentHomeBinding
import com.brian.internals.ClickGuard
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class HomeFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: FragmentHomeBinding
    lateinit var mViewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        mBinding.toolbar.tvTitle.setText(getString(R.string.baseball_bee))
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigate(R.id.mainScreenFragment)
        }

        mViewModel.getDefensive()

        setupClickListeners()

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun setupClickListeners() {

        mBinding.apply {
            Pitcher.setOnClickListener { clickHandler!!.onPitcherClick() }
            Catcher.setOnClickListener { clickHandler!!.onCatcherClick() }
            First.setOnClickListener { clickHandler!!.onFirstBaseClick() }
            SecondBase.setOnClickListener { clickHandler!!.onSecondBaseClick() }
            Third.setOnClickListener { clickHandler!!.onThirdBaseClick() }
            ShortStep.setOnClickListener { clickHandler!!.onShortStepClick() }
            LeftField.setOnClickListener { clickHandler!!.onLeftFieldClick() }
            Center.setOnClickListener { clickHandler!!.onCenterFieldClick() }
            RightField.setOnClickListener { clickHandler!!.onRightFieldClick() }

            ClickGuard.guard(
                Pitcher,
                Catcher,
                First,
                SecondBase,
                Third,
                ShortStep,
                LeftField,
                Center,
                RightField
            )
        }

    }

    inner class ClickHandler {

        fun onPitcherClick() {
            findNavController().navigate(
                R.id.pitcherFragment,
                bundleOf(
                    getString(R.string.description) to mViewModel.list[0].description,
                    getString(R.string.name) to getString(R.string.pitcher),
                    getString(
                        R.string.id
                    ) to mViewModel.list[0].id
                )
            )
        }

        fun onCatcherClick() {
            findNavController().navigate(
                R.id.pitcherFragment,
                bundleOf(
                    getString(R.string.description) to mViewModel.list[1].description,
                    getString(R.string.name) to getString(R.string.catcher),
                    getString(
                        R.string.id
                    ) to mViewModel.list[1].id
                )
            )
        }

        fun onFirstBaseClick() {
            findNavController().navigate(
                R.id.pitcherFragment,
                bundleOf(
                    getString(R.string.description) to mViewModel.list[2].description,
                    getString(R.string.name) to getString(R.string.first_base),
                    getString(
                        R.string.id
                    ) to mViewModel.list[2].id
                )
            )
        }

        fun onSecondBaseClick() {
            findNavController().navigate(
                R.id.pitcherFragment,
                bundleOf(
                    getString(R.string.description) to mViewModel.list[3].description,
                    getString(R.string.name) to getString(R.string.second_base),
                    getString(
                        R.string.id
                    ) to mViewModel.list[3].id
                )
            )
        }

        fun onThirdBaseClick() {
            findNavController().navigate(
                R.id.pitcherFragment,
                bundleOf(
                    getString(R.string.description) to mViewModel.list[4].description,
                    getString(R.string.name) to getString(R.string.third_base),
                    getString(
                        R.string.id
                    ) to mViewModel.list[4].id
                )
            )
        }

        fun onShortStepClick() {
            findNavController().navigate(
                R.id.pitcherFragment,
                bundleOf(
                    getString(R.string.description) to mViewModel.list[5].description,
                    getString(R.string.name) to getString(R.string.short_step),
                    getString(
                        R.string.id
                    ) to mViewModel.list[5].id
                )
            )
        }

        fun onLeftFieldClick() {
            findNavController().navigate(
                R.id.pitcherFragment,
                bundleOf(
                    getString(R.string.description) to mViewModel.list[6].description,
                    getString(R.string.name) to getString(R.string.left_field),
                    getString(
                        R.string.id
                    ) to mViewModel.list[6].id
                )
            )
        }

        fun onCenterFieldClick() {
            findNavController().navigate(
                R.id.pitcherFragment,
                bundleOf(
                    getString(R.string.description) to mViewModel.list[7].description,
                    getString(R.string.name) to getString(R.string.center_field),
                    getString(
                        R.string.id
                    ) to mViewModel.list[7].id
                )
            )
        }

        fun onRightFieldClick() {
            findNavController().navigate(
                R.id.pitcherFragment,
                bundleOf(
                    getString(R.string.description) to mViewModel.list[8].description,
                    getString(R.string.name) to getString(R.string.right_field),
                    getString(
                        R.string.id
                    ) to mViewModel.list[8].id
                )
            )
        }
    }
}