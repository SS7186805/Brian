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
import com.brian.databinding.ChangePasswordFragmentBinding
import com.brian.internals.DialogUtil
import com.brian.viewModels.myProfile.MyProfileViewModel
import com.brian.viewModels.myProfile.MyProfileViewModelFactory
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.activities.AccountHandlerActivity
import com.brian.views.activities.HomeActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.change_password_fragment.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.progress_bar
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ChangePasswordFragment : ScopedFragment(), KodeinAware, DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: MyProfileViewModelFactory by instance()
    lateinit var mBinding: ChangePasswordFragmentBinding
    lateinit var mViewModel: MyProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        setupObserver()
        mBinding = ChangePasswordFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = getString(R.string.change_password)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(MyProfileViewModel::class.java)
    }

    private fun setupObserver(){
        mViewModel.apply {
            showLoading.observe(viewLifecycleOwner, Observer {
                if (it)
                    progress_bar.visibility = View.VISIBLE
            })

            showMessage.observe(viewLifecycleOwner, Observer {
                progress_bar.visibility = View.GONE
                if (!TextUtils.isEmpty(it))
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                if(it == "Password has been changed successfully."){
                    DialogUtil.build(requireContext()) {
                        title = getString(R.string.success)
                        dialogType = DialogUtil.DialogType.SUCCESS
                        message = getString(R.string.password_message)
                        successClickListener = this@ChangePasswordFragment
                    }
                }
            })
        }
    }

    inner class ClickHandler {

        fun onCreateClick() {
            DialogUtil.build(requireContext()) {
                title = getString(R.string.success)
                dialogType = DialogUtil.DialogType.SUCCESS
                message = getString(R.string.password_message)
                successClickListener = this@ChangePasswordFragment
            }
        }


    }

    override fun onOkayClick() {
        Prefs.init().isLogIn = "false"
        startActivity(Intent(requireContext(), AccountHandlerActivity::class.java))
        (requireActivity() as HomeActivity).finish()
    }
}