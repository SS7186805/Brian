package com.brian.views.fragments

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.ContactUsFragmentBinding
import com.brian.internals.*
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ContactUsFragment : ScopedFragment(), KodeinAware, DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: ContactUsFragmentBinding
    lateinit var mViewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = ContactUsFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        mBinding.message.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mBinding.message.setRawInputType(InputType.TYPE_CLASS_TEXT);

        mBinding.title.clearFocus()



        setupObserver()
        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }


    inner class ClickHandler {

        fun onCreateClick() {

            if (Validator.init.validateContactUsData(mBinding, requireContext())) {
                mViewModel.contactUsParams.title = mBinding.title.text.toString()
                mViewModel.contactUsParams.description = mBinding.message.text.toString()
                mViewModel.contactUs()

            }

        }


    }

    override fun onOkayClick() {
        findNavController().navigate(R.id.mainScreenFragment)

    }


    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {
                    requireContext().showToast(it)
                    showMessage.postValue("")
                }
            })



            contactUsResponse.observe(viewLifecycleOwner, Observer {
                if (it.result?.contains(getString(R.string.success))!!) {
                    DialogUtil.build(requireContext()) {
                        title = getString(R.string.success)
                        dialogType = DialogUtil.DialogType.SUCCESS
                        message = getString(R.string.contact_us_message)
                        successClickListener = this@ContactUsFragment
                    }
                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }


}