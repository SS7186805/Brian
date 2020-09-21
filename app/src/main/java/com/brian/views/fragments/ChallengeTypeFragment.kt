package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.*
import com.brian.internals.DialogUtil
import com.brian.viewModels.login.LoginViewModel
import com.brian.viewModels.login.LoginViewModelFactory
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ChallengeTypeFragment : ScopedFragment(), KodeinAware,DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: ChallengeTypeFragmentBinding
    lateinit var mViewModel: RegisterViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = ChallengeTypeFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text=getString(R.string.challenge_type)
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



    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(arguments?.getString("badges").equals("badges")){

            mBinding.toolbar.tvTitle.setText(getString(R.string.badges_earned))

        }
    }

    override fun onOkayClick() {
    }
}