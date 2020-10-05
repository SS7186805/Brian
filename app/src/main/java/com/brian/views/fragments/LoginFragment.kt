package com.brian.views.fragments

import android.content.Intent
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
import com.brian.base.Prefs
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentLoginBinding
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.activities.AccountHandlerActivity
import com.brian.views.activities.HomeActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.progress_bar
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
        setupObserver()
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
            clearFields()
            findNavController().navigate(R.id.forgotPasswordFragment)
        }

        fun onRegisterClick() {
            clearFields()
            findNavController().navigate(R.id.registerFragment)
        }

        fun onLoginClick() {
            clearFields()
            Prefs.init().isLogIn = "true"
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            (requireActivity() as AccountHandlerActivity).finish()

        }
    }

    private fun setupObserver(){
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                progress_bar.visibility = View.GONE
                if (!TextUtils.isEmpty(it)){
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
                if(it == "User logged in successfully."){
                    clearFields()
                    Prefs.init().isLogIn = "true"
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    (requireActivity() as AccountHandlerActivity).finish()
                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if(it){
                    progress_bar.visibility = View.VISIBLE
                }
            })
        }
    }

    private fun clearFields() {
        mBinding.etUserName.text.clear()
        mBinding.etPassword.text.clear()
    }
}