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
import com.brian.databinding.CreateChallengeFragmentBinding
import com.brian.databinding.FragmentLoginBinding
import com.brian.databinding.PitcherFragmentBinding
import com.brian.databinding.TrainingVideosBinding
import com.brian.internals.DialogUtil
import com.brian.internals.Utils
import com.brian.viewModels.login.LoginViewModel
import com.brian.viewModels.login.LoginViewModelFactory
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class CreateChallengeFragment : ScopedFragment(), KodeinAware,DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: CreateChallengeFragmentBinding
    lateinit var mViewModel: RegisterViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = CreateChallengeFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text=getString(R.string.create_challenge)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    inner class ClickHandler{

        fun onCreateClick(){
            DialogUtil.build(requireContext()) {
                title = getString(R.string.success)
                dialogType = DialogUtil.DialogType.SUCCESS
                message = getString(R.string.challenge_created)
                successClickListener = this@CreateChallengeFragment
            }
        }

        fun onSelectChallenge(){
            findNavController().navigate(R.id.challengeType)
        }

        fun onSelectUser(){

                findNavController().navigate(R.id.usersFragment, bundleOf("no" to "no"))

        }
        fun selectDate(){
            hideKeyboard(requireView())
            Utils.init.selectDate(requireContext(),
                Utils.init.getCurrentDate(),mBinding.selectDate,false)
        }

    }

    override fun onOkayClick() {
        findNavController().navigateUp()
    }
}