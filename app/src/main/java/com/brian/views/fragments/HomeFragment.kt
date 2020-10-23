package com.brian.views.fragments

import android.app.Activity
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
        mBinding.toolbar.ivBack.setOnClickListener{
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

    private fun setupClickListeners(){

        mBinding.apply {
            Pitcher.setOnClickListener{ clickHandler!!.onPitcherClick()}
            Catcher.setOnClickListener{clickHandler!!.onCatcherClick()}
            First.setOnClickListener{clickHandler!!.onFirstBaseClick()}
            SecondBase.setOnClickListener{clickHandler!!.onSecondBaseClick()}
            Third.setOnClickListener{clickHandler!!.onThirdBaseClick()}
            ShortStep.setOnClickListener{clickHandler!!.onShortStepClick()}
            LeftField.setOnClickListener{clickHandler!!.onLeftFieldClick()}
            Center.setOnClickListener{clickHandler!!.onCenterFieldClick()}
            RightField.setOnClickListener{clickHandler!!.onRightFieldClick()}

            ClickGuard.guard(Pitcher,Catcher,First,SecondBase,Third,ShortStep,LeftField,Center,RightField)
        }

    }
    inner class ClickHandler{

        fun onPitcherClick(){
            findNavController().navigate(R.id.pitcherFragment, bundleOf("list" to mViewModel.list,"name" to "Pitcher" ))
        }
        fun onCatcherClick(){
            findNavController().navigate(R.id.pitcherFragment, bundleOf("list" to mViewModel.list,"name" to "Catcher" ))
        }
        fun onFirstBaseClick(){
            findNavController().navigate(R.id.pitcherFragment, bundleOf("list" to mViewModel.list,"name" to "First Base" ))
        }
        fun onSecondBaseClick(){
            findNavController().navigate(R.id.pitcherFragment, bundleOf("list" to mViewModel.list,"name" to "Second Base" ))
        }
        fun onThirdBaseClick(){
            findNavController().navigate(R.id.pitcherFragment, bundleOf("list" to mViewModel.list,"name" to "Third Base" ))
        }
        fun onShortStepClick(){
            findNavController().navigate(R.id.pitcherFragment, bundleOf("list" to mViewModel.list,"name" to "Short Step" ))
        }
        fun onLeftFieldClick(){
            findNavController().navigate(R.id.pitcherFragment, bundleOf("list" to mViewModel.list,"name" to "Left Field" ))
        }
        fun onCenterFieldClick(){
            findNavController().navigate(R.id.pitcherFragment, bundleOf("list" to mViewModel.list,"name" to "Center Field" ))
        }
        fun onRightFieldClick(){
            findNavController().navigate(R.id.pitcherFragment, bundleOf("list" to mViewModel.list,"name" to "Right Field" ))
        }
    }
}