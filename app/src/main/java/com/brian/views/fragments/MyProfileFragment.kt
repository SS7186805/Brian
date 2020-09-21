package com.brian.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.Prefs
import com.brian.base.ScopedFragment
import com.brian.databinding.MyProfileFragmentBinding
import com.brian.internals.DialogUtil
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.activities.AccountHandlerActivity
import com.brian.views.activities.HomeActivity
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MyProfileFragment : ScopedFragment(), KodeinAware, DialogUtil.YesNoDialogClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: MyProfileFragmentBinding
    lateinit var mViewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = MyProfileFragmentBinding.inflate(inflater, container, false).apply {
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

        fun onViewProfileClick() {
            findNavController().navigate(R.id.userProfileFragment)

        }

        fun onEditProfileClick() {
            findNavController().navigate(R.id.register, bundleOf("edit" to "edit"))

        }

        fun onBadgesEarnedClick() {
            findNavController().navigate(R.id.challengeType, bundleOf("badges" to "badges"))

        }

        fun onMyTeamsClick() {
            findNavController().navigate(R.id.myTeamfragment)

        }

        fun onMyChallengesClick() {
            findNavController().navigate(R.id.myChallengesFragment)
        }

        fun onChangePasswordClick() {
            findNavController().navigate(R.id.changePasswordFragment)
        }

        fun onLogoutClick() {
            DialogUtil.build(requireContext()) {
                title = getString(R.string.log_out)
                dialogType = DialogUtil.DialogType.LOGOUT
                message = getString(R.string.log_out_message)
                yesNoDialogClickListener = this@MyProfileFragment
            }
        }

    }

    override fun onClickYes() {
        Prefs.init().isLogIn="false"
        startActivity(Intent(requireContext(),AccountHandlerActivity::class.java))
        (requireActivity() as HomeActivity).finish()

    }

    override fun onClickNo() {

    }


}