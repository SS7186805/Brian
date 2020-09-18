package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.ChallengeFragmentBinding
import com.brian.internals.DialogUtil
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import kotlinx.android.synthetic.main.activity_main.view.ivBack
import kotlinx.android.synthetic.main.activity_main.view.tvTitle
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ChallengeFragment : ScopedFragment(), KodeinAware,DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: ChallengeFragmentBinding
    lateinit var mViewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = ChallengeFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = getString(R.string.challenge)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.toolbar.ivShare.visibility = VISIBLE
        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    inner class ClickHandler {

        fun onSubmitClick() {
            DialogUtil.build(requireContext()) {
                title = getString(R.string.success)
                dialogType = DialogUtil.DialogType.SUCCESS
                message = getString(R.string.challenge_submitted)
                successClickListener = this@ChallengeFragment
            }
        }

    }

    override fun onOkayClick() {

        findNavController().navigateUp()

    }
}