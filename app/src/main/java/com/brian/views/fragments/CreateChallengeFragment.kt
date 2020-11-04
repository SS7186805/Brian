package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.CreateChallengeFragmentBinding
import com.brian.internals.*
import com.brian.models.MyFriendsDataItem
import com.brian.viewModels.challenges.ChallengesViewModel
import com.brian.viewModels.challenges.ChallengesViewModelFactory
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class CreateChallengeFragment : ScopedFragment(), KodeinAware, DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: ChallengesViewModelFactory by instance()
    lateinit var mBinding: CreateChallengeFragmentBinding
    lateinit var mViewModel: ChallengesViewModel
    var newChallenge = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = CreateChallengeFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = getString(R.string.create_challenge)
        mBinding.toolbar.ivBack.setOnClickListener {
            getActivity()?.getIntent()?.removeExtra("id")
            getActivity()?.getIntent()?.removeExtra("key")
            findNavController().navigateUp()
        }

        mBinding.challengeTitle.clearFocus()

        Log.e("OnCreateChallenge", "sdsd")
        setupObserver()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun clear() {
        activity?.viewModelStore?.clear()
    }

    override fun onResume() {
        super.onResume()
        if (getActivity()?.getIntent()?.getExtras()?.getParcelableArrayList<MyFriendsDataItem>(
                "key"
            ) != null
        ) {
            setUsersData()

        }
        if (getActivity()?.getIntent()?.getExtras()?.getString(getString(R.string.id)) != null) {
            mViewModel.createChallengeParams.challenge_id =
                getActivity()?.getIntent()?.getExtras()?.getString(getString(R.string.id))
                    ?.toInt()

            mBinding.selectChallengeType.setText(
                getActivity()?.getIntent()?.getExtras()?.getString(
                    getString(R.string.title)
                )
            )

        }
    }


    fun setUsersData() {
        var selectedUsers = getActivity()?.getIntent()?.getExtras()
            ?.getParcelableArrayList<MyFriendsDataItem>("key")
        var selectedUsersNames = ""
        var selecteduserIds = ""

        if (selectedUsers?.isNotEmpty()!!) {

            for (user in selectedUsers!!) {
                selectedUsersNames = "${selectedUsersNames}, ${user.uname}"
            }

            for (user in selectedUsers!!) {
                selecteduserIds = "${selecteduserIds}, ${user.otherUserDetail?.id}"
            }

            selectedUsersNames =
                selectedUsersNames.substring(1, selectedUsersNames.length).trim()

            selecteduserIds =
                selecteduserIds.substring(1, selecteduserIds.length).trim()
            mBinding.selectUser.setText(selectedUsersNames)
            mViewModel.createChallengeParams.challenge_to_user = selecteduserIds
        }
    }

    fun clearData() {
        mBinding.challengeTitle.setText("")
        mBinding.selectUser.setText("")
        mBinding.selectDate.setText("")
        mBinding.selectChallengeType.setText("")

    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChallengesViewModel::class.java)
    }

    inner class ClickHandler {

        fun onCreateClick() {
            if (Validator.init.validateCreateChallengeData(mBinding, requireContext())) {
                mViewModel.createChallengeParams.date_and_time = mBinding.selectDate.text.toString()
                mViewModel.createChallengeParams.challenge_title =
                    mBinding.challengeTitle.text.toString()
                mViewModel.createChallenge()
            }

        }

        fun onSelectChallenge() {
            findNavController().navigate(R.id.challengeType)
        }

        fun onSelectUser() {

            findNavController().navigate(
                R.id.usersFragment,
                bundleOf(
                    getString(R.string.challenge_type) to getString(R.string.yes),
                    getString(R.string.no) to getString(R.string.no)
                )
            )


        }

        fun selectDate() {
            hideKeyboard(requireView())
            Utils.init.selectDateWithTime(
                requireContext(),
                Utils.init.getCurrentDate(), mBinding.selectDate, false
            )
        }

    }


    override fun onOkayClick() {
        clear()
        findNavController().navigateUp()
    }

    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {
                    requireContext().showToast(it)
                    showMessage.postValue("")
                }
            })

            createChallengeResponse.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    if (it.result?.contains(getString(R.string.success))!!) {
                        DialogUtil.build(requireContext()) {
                            title = getString(R.string.success)
                            dialogType = DialogUtil.DialogType.SUCCESS
                            message = getString(R.string.challenge_created)
                            successClickListener = this@CreateChallengeFragment
                        }
                    }
                }


            })


            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }


}