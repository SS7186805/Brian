package com.brian.views.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.Prefs
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentLoginBinding
import com.brian.internals.ClickGuard
import com.brian.internals.keyboardListener
import com.brian.internals.showToast
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.activities.AccountHandlerActivity
import com.brian.views.activities.HomeActivity
import kotlinx.android.synthetic.main.fragment_register.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
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
        setupClickListeners()
      //  keyboardListener()


//        mBinding.etPassword.setOnEditorActionListener(object : TextView.OnEditorActionListener {
//            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    mBinding.root.requestFocus()
//                }
//                return true
//            }
//        })
        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    private fun setupClickListeners() {
        mBinding.apply {
            btnLogin.setOnClickListener { mViewModel.onLoginClick() }

            ClickGuard.guard(btnLogin)
        }
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

    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {
                    requireContext().showToast(it)
                    showMessage.postValue("")
                }
            })

            registerSuccess.observe(viewLifecycleOwner, Observer {
                if(it){
                    Prefs.init().isLogIn = "true"
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    (requireActivity() as AccountHandlerActivity).finish()
                }
            })

            showLoading.observe(viewLifecycleOwner, Observer {
                progress_bar.visibility = if (it) VISIBLE else GONE
            })
        }
    }

    private fun clearFields() {
        mBinding.etUserName.text.clear()
        mBinding.etPassword.text.clear()
    }

    fun keyboardListener() {
        requireActivity().keyboardListener { isOpen ->
            if (!isOpen) {
                /* mBinding.btnLogin.requestFocus()
                 mBinding.btnLogin.requestFocusFromTouch()
                 mBinding.etPassword.requestFocus()
                 mBinding.etPassword.requestFocusFromTouch()*/
            }
        }
    }
}