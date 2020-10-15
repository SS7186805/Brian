package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentForgotPasswordBinding
import com.brian.databinding.FragmentLoginBinding
import com.brian.internals.ClickGuard
import com.brian.internals.DialogUtil
import com.brian.viewModels.login.LoginViewModel
import com.brian.viewModels.login.LoginViewModelFactory
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ForgotPasswordFragment : ScopedFragment(), KodeinAware,DialogUtil.SuccessClickListener  {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: FragmentForgotPasswordBinding
    lateinit var mViewModel: RegisterViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        setupObserver()
        mBinding = FragmentForgotPasswordBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        mBinding.toolbar.tvTitle.text=getString(R.string.forgot_password_title)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setupClickListeners()
        return mBinding.root
    }

    private fun setupClickListeners() {
        mBinding.apply {
            btnSend.setOnClickListener { mViewModel.onSendClick() }
            ClickGuard.guard(btnSend)
        }
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    inner class ClickHandler{

        fun onSendClick(){
            DialogUtil.build(requireContext()) {
                title = getString(R.string.success)
                dialogType = DialogUtil.DialogType.SUCCESS
                message = getString(R.string.reset_password_mesage)
                successClickListener = this@ForgotPasswordFragment
            }
        }

    }

    private fun setupObserver(){
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                progress_bar.visibility = View.GONE
                if (!TextUtils.isEmpty(it)){
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            })
            registerSuccess.observe(viewLifecycleOwner, Observer {
                if (it) {
                    DialogUtil.build(requireContext()) {
                        title = getString(R.string.success)
                        dialogType = DialogUtil.DialogType.SUCCESS
                        message = getString(R.string.reset_password_mesage)
                        successClickListener = this@ForgotPasswordFragment
                    }
                }
            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if(it){
                    progress_bar.visibility = View.VISIBLE
                }
            })
        }

    }

    override fun onOkayClick() {

        findNavController().navigate(R.id.loginFragment)

    }
}