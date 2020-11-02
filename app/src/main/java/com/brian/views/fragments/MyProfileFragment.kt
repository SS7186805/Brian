package com.brian.views.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.Prefs
import com.brian.base.ScopedFragment
import com.brian.databinding.MyProfileFragmentBinding
import com.brian.internals.ClickGuard
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
        setupObserver()
        mBinding = MyProfileFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        setupClickListeners()

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {

                }
                //Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun setupClickListeners() {
        mBinding.apply {
            viewProfile.setOnClickListener { clickHandler!!.onViewProfileClick() }
            editProfile.setOnClickListener { clickHandler!!.onEditProfileClick() }
            changePassword.setOnClickListener { clickHandler!!.onChangePasswordClick() }
            myChallenges.setOnClickListener { clickHandler!!.onMyChallengesClick() }
            badgesEarned.setOnClickListener { clickHandler!!.onBadgesEarnedClick() }
            myTeams.setOnClickListener { clickHandler!!.onMyTeamsClick() }
            logout.setOnClickListener { clickHandler!!.onLogoutClick() }

            ClickGuard.guard(
                viewProfile,
                editProfile,
                changePassword,
                myChallenges,
                myTeams,
                badgesEarned,
                logout
            )
        }
    }


    inner class ClickHandler {

        fun onViewProfileClick() {
            findNavController().navigate(
                R.id.userProfileFragment,
                bundleOf(getString(R.string.user_id) to Prefs.init().userInfo?.id.toString())
            )

        }

        fun onEditProfileClick() {
            findNavController().navigate(R.id.register, bundleOf("edit" to "edit"))

        }

        fun onBadgesEarnedClick() {
            findNavController().navigate(R.id.challengeType, bundleOf("badges" to "badges"))

        }

        fun onMyTeamsClick() {
            findNavController().navigate(R.id.teamFragment)

        }

        fun onMyChallengesClick() {
//            findNavController().navigate(R.id.myChallengesFragment)
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
        mViewModel.logOut()
        Prefs.init().isLogIn = "false"
        Toast.makeText(requireContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(requireContext(), AccountHandlerActivity::class.java))
        (requireActivity() as HomeActivity).finish()
    }

    override fun onClickNo() {

    }


}