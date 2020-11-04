package com.brian.views.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.CreateTeamFragmentBinding
import com.brian.internals.*
import com.brian.models.MyFriendsDataItem
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.github.dhaval2404.imagepicker.ImagePicker
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_main.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.File

class CreateTeamFragment : ScopedFragment(), KodeinAware, DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: CreateTeamFragmentBinding
    lateinit var mViewModel: HomeViewModel
    private var imageFile: File? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = CreateTeamFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        mBinding.toolbar.tvTitle.text = getString(R.string.create_team)
        mBinding.toolbar.ivBack.setOnClickListener {
            activity?.intent?.removeExtra("key")
            findNavController().navigateUp()
        }

        mBinding.etTeamName.clearFocus()

        setupObserver()

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    inner class ClickHandler {

        fun onCreateClick() {

            if (Validator.init.validateCreateTeam(mBinding, requireContext())) {
                mViewModel.createTeamParams.team_name = mBinding.etTeamName.text.toString()

                mViewModel.createTeam()
            }

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


        fun onSelectImage() {
            runWithPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
                addProfile()
            }
        }

    }

    override fun onOkayClick() {
        findNavController().navigateUp()

    }

    //add profile
    private fun addProfile() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                mBinding.ivImage.setImageURI(fileUri)
                //You can get File object from intent
                imageFile = ImagePicker.getFile(data)!!

                val filePart = MultipartBody.Part.createFormData(
                    "image",
                    imageFile?.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), imageFile!!)
                )

                mViewModel.createTeamParams.image = filePart


            }
            ImagePicker.RESULT_ERROR -> {
//                showMessageInSnack(binding.root, ImagePicker.getError(data))
            }
            else -> {
//                showMessageInSnack(binding.root, "Task Cancelled")
            }
        }
    }

    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {
                    if (it.equals(getString(R.string.success))) {
                        DialogUtil.build(requireContext()) {
                            title = getString(R.string.success)
                            dialogType = DialogUtil.DialogType.SUCCESS
                            message = getString(R.string.team_created)
                            successClickListener = this@CreateTeamFragment
                        }
                    } else {
                        requireContext().showToast(it)
                    }
                    showMessage.postValue("")

                }
            })

            myTeams.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    mViewModel.myTeamsAdapter.addNewItems(it)
                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }


    override fun onResume() {
        super.onResume()

        if (getActivity()?.getIntent()?.getExtras()?.getParcelableArrayList<MyFriendsDataItem>("key") != null) {
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
                mBinding.etSelectUser.setText(selectedUsersNames)
                mViewModel.createTeamParams.users = selecteduserIds
            }
        }
    }


}