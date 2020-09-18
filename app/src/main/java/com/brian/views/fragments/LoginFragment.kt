package com.brian.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.Prefs
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentLoginBinding
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.activities.AccountHandlerActivity
import com.brian.views.activities.HomeActivity
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class LoginFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: FragmentLoginBinding
    lateinit var mViewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    inner class ClickHandler {
        fun onForgotPasswordClick() {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }

        fun onRegisterClick() {
            findNavController().navigate(R.id.registerFragment)
        }

        fun onLoginClick() {
            Prefs.init().isLogIn = "true"
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            (requireActivity() as AccountHandlerActivity).finish()

        }
    }
}