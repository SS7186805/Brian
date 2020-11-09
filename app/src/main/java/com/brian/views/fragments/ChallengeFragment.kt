package com.brian.views.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.PathUtil
import com.brian.base.ScopedFragment
import com.brian.databinding.ChallengeFragmentBinding
import com.brian.internals.*
import com.brian.models.DataItemMyChalleneges
import com.brian.viewModels.challenges.ChallengesViewModel
import com.brian.viewModels.challenges.ChallengesViewModelFactory
import com.bumptech.glide.Glide
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.vincent.videocompressor.VideoCompress
import kotlinx.android.synthetic.main.activity_main.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.*
import java.util.*


class ChallengeFragment : ScopedFragment(), KodeinAware, DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: ChallengesViewModelFactory by instance()
    lateinit var mBinding: ChallengeFragmentBinding
    lateinit var mViewModel: ChallengesViewModel
    private var videoFile: File? = null
    var DOWNLOAD_PATH =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .absolutePath


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

        setupObserver()

        mBinding.item =
            arguments?.getParcelable<DataItemMyChalleneges>(getString(R.string.challenge))!!
        mViewModel.acceptChallengeRequestParams.user_challenge_id = mBinding.item?.id
        mViewModel.acceptChallengeRequestParams.status = 1


     /*   if (android.text.format.DateFormat.is24HourFormat(requireContext())) {
            mBinding.time.setText(Utils.init.get24HourTimeChallenge(mBinding.item!!.challenge?.createdAt.toString()))
        } else {
            mBinding.time.setText(Utils.init.get12HourTimeChallenge(mBinding.item!!.challenge?.createdAt.toString()))
        }*/
//        mBinding.toolbar.ivShare.visibility = VISIBLE
        return mBinding.root
    }

    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {
                    if (it.contains(getString(R.string.request_accept))) {

                        DialogUtil.build(requireContext()) {
                            title = getString(R.string.success)
                            dialogType = DialogUtil.DialogType.SUCCESS
                            message = getString(R.string.challenge_submitted)
                            successClickListener = this@ChallengeFragment
                        }

                    } else {
                        requireContext().showToast(it)
                    }

                    showMessage.postValue("")
                }
            })



            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChallengesViewModel::class.java)
    }

    inner class ClickHandler {


        fun addVideo() {
            runWithPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
                showPictureDialog()
            }
        }

        fun onSubmitClick() {

            if (mViewModel.acceptChallengeRequestParams.file_name == null) {
                requireContext().showToast(getString(R.string.select_video_message))
            } else {

                mViewModel.acceptChallengeRequests()
            }

        }

    }

    override fun onOkayClick() {

        findNavController().navigateUp()

    }

    fun selectVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60)
        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),
            100
        )
    }


    private fun showPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Gallery",
            "Camera"
        )
        pictureDialog.setItems(pictureDialogItems,
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> selectVideo()
                    1 -> takeVideoFromCamera()
                }
            })
        pictureDialog.show()
    }

    private fun takeVideoFromCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60)
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                val selectedImageUri: Uri? = data?.data
                var filePath = PathUtil.getPath(requireContext(), selectedImageUri!!)
                if (Validator.init.validatePostMediaData(requireContext(), filePath)) {
                    videoFile = File(filePath)
                    val filePart = MultipartBody.Part.createFormData(
                        "file_name",
                        videoFile?.name,
                        RequestBody.create("video/*".toMediaTypeOrNull(), videoFile!!)

                    )


                    val destPath: String =
                        DOWNLOAD_PATH + File.separator + "POST_VID_" + Calendar.getInstance().timeInMillis + ".mp4"
                    VideoCompress.compressVideoLow(
                        filePath,
                        destPath,
                        object : VideoCompress.CompressListener {
                            override fun onStart() {
                                showProgress(requireContext())
                            }

                            override fun onSuccess() {
                                hideProgress()
                                Glide.with(requireContext())
                                    .asBitmap()
                                    .load(selectedImageUri) // or URI/path
                                    .into(mBinding.videoView)
                                mViewModel.acceptChallengeRequestParams.file_name = filePart
                            }

                            override fun onFail() {
                                mViewModel.showLoading.postValue(false)

                            }

                            override fun onProgress(percent: Float) {}
                        })

                }

            }

            if (requestCode == 101) {
                val selectedImageUri: Uri = data?.getData()!!

                var selectedImagePath = PathUtil.getPath(requireContext(), selectedImageUri!!)

                if (Validator.init.validatePostMediaData(requireContext(), selectedImagePath)) {
                    videoFile = File(selectedImagePath)
                    val filePart = MultipartBody.Part.createFormData(
                        "file_name",
                        videoFile?.name,
                        RequestBody.create("video/*".toMediaTypeOrNull(), videoFile!!)

                    )

                    Log.e("ImageUri", "####${selectedImageUri}")

                    val destPath: String =
                        DOWNLOAD_PATH + File.separator + "POST_VID_" + Calendar.getInstance().timeInMillis + ".mp4"

                    VideoCompress.compressVideoLow(
                        selectedImagePath,
                        destPath,
                        object : VideoCompress.CompressListener {
                            override fun onStart() {
                                showProgress(requireContext())
                            }

                            override fun onSuccess() {
                                hideProgress()
                                Glide.with(requireContext())
                                    .asBitmap()
                                    .load(selectedImageUri) // or URI/path
                                    .into(mBinding.videoView)
                                mViewModel.acceptChallengeRequestParams.file_name = filePart
                            }

                            override fun onFail() {
                                mViewModel.showLoading.postValue(false)

                            }

                            override fun onProgress(percent: Float) {}
                        })

                }


            }
        }
    }


    fun writeFile(`in`: InputStream, file: File?) {
        var out: OutputStream? = null
        try {
            out = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (`in`.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (out != null) {
                    out.close()
                }
                `in`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    // UPDATED!
    fun getPath(uri: Uri?): String? {
        val projection =
            arrayOf(MediaStore.Video.Media.DATA)
        val cursor: Cursor =
            requireContext().getContentResolver().query(uri!!, projection, null, null, null)!!
        return if (cursor != null) { // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }

}