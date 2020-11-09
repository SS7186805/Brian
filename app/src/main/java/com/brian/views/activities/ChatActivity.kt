package com.brian.views.activities

import android.Manifest
import android.app.Activity
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
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.base.EndlessChatScrollListener
import com.brian.base.MainApplication
import com.brian.base.PathUtil
import com.brian.base.ScopedActivity
import com.brian.databinding.ChatFragmentBinding
import com.brian.internals.Validator
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.models.AllMessagesDataItem
import com.brian.models.SendMessageParams
import com.brian.viewModels.messages.MessagesViewModel
import com.brian.viewModels.messages.MessagesViewModelFactory
import com.brian.views.adapters.ChatAdapter
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.vincent.videocompressor.VideoCompress
import kotlinx.android.synthetic.main.activity_main.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : ScopedActivity(), KodeinAware, ChatAdapter.onClick {
    override val kodein by lazy { (applicationContext as KodeinAware).kodein }
    private val viewModelFactory: MessagesViewModelFactory by instance()
    lateinit var mBinding: ChatFragmentBinding
    lateinit var mViewModel: MessagesViewModel
    var chatRoomId = 0
    var otherUserId = 0
    private var videoFile: File? = null
    var mEndlessFriendsecyclerViewScrollListener: EndlessChatScrollListener? = null
    var finalList = ArrayList<AllMessagesDataItem>()
    var DOWNLOAD_PATH =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .absolutePath


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setupViewModel()
        mBinding = DataBindingUtil.setContentView(this, R.layout.chat_fragment)
        mBinding.apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = intent.getStringExtra(getString(R.string.user))

        chatRoomId = intent.getIntExtra(getString(R.string.chat_room_id), 0)
        otherUserId = intent.getIntExtra(getString(R.string.other_user_id), 0)
        mViewModel.createChatRoomParams.other_user_id = otherUserId



        mBinding.chatRecycler.adapter = mViewModel.chatAdapter
        mViewModel.chatAdapter.listener = this


        mBinding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }

        mViewModel.createChatRoom()
//        setupScrollListener()
        keyboardOpenChangListener()
        onSwipe()


        setupObserver()
    }

    fun onSwipe() {
        mBinding.lSwipe.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.yellow));
        mBinding.lSwipe.setOnRefreshListener {

            if (mViewModel.allMessages.value!!.size % 10 == 0 && mViewModel.allMessages.value!!.size != 0) {
                mViewModel.getAllMessages()
            }

            mBinding.lSwipe.isRefreshing = false

        }
    }

    fun keyboardOpenChangListener() {
        mBinding.chatRecycler.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                Log.e("OlDD", "sd")
                mBinding.chatRecycler.postDelayed(object : Runnable {
                    override fun run() {
                        mBinding.chatRecycler.smoothScrollToPosition(finalList.size!!);
                    }
                }, 100);
            }
        }
    }


    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(MessagesViewModel::class.java)
    }

    inner class ClickHandler {


        fun onSendMessage() {

            if (MainApplication.hasNetwork()) {
                sendMessage()

            } else {
                showToast(getString(R.string.noInternetErr))
            }


        }


        fun onSelectVideo() {
            runWithPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
                showPictureDialog()
            }
        }


    }

    fun sendMessage() {
        val message: String = mBinding.etMessage.getText().toString().trim()
        if (!TextUtils.isEmpty(message)) {
            val messageParams =
                SendMessageParams()
            messageParams.message = message
            messageParams.chat_room_id = chatRoomId
            messageParams.other_user_id = otherUserId
            mViewModel.sendMessage(messageParams)

        } else {
            showToast(getString(R.string.enter_message_validation))
        }
        mBinding.etMessage.setText("")
    }

    fun sendVideoMessage(file: MultipartBody.Part) {

        val messageParams =
            SendMessageParams()
        messageParams.file_name = file
        messageParams.type_of_file = "video"
        messageParams.chat_room_id = chatRoomId
        messageParams.other_user_id = otherUserId
        mViewModel.sendVideoMessage(messageParams)


    }


    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(this@ChatActivity, Observer {
                if (!TextUtils.isEmpty(it)) {
                    showToast(it)
                    showMessage.postValue("")
                }
            })

            resCreateChatRoomParams.observe(this@ChatActivity, Observer {
                if (it.result?.contains(getString(R.string.success))!!) {
                    mViewModel.getAllChatParams.chat_room_id = it.data?.id
                    chatRoomId = mViewModel.getAllChatParams.chat_room_id?.toInt()!!
                    mViewModel.getAllMessages()
                }
            })

            sendMessageResponse.observe(this@ChatActivity, Observer {
                if (it.result?.contains(getString(R.string.success))!!) {
                    if (finalList?.size != 0) {
                        finalList?.add(
                            finalList.size,
                            it.data!!
                        )
                    } else {
                        finalList.add(
                            it.data!!
                        )
                    }
                    mViewModel.chatAdapter.setNewItems(finalList)
                    mBinding.chatRecycler.scrollToPosition(mViewModel.allMessages.value?.size!! - 1)

                }

            })

            allMessages.observe(this@ChatActivity, Observer {
                Collections.reverse(it)
                it.addAll(finalList)
                finalList = it
                if (it != null && it.isNotEmpty()) {
                    Log.e("messagessize", allMessages.value?.size.toString())
                    mViewModel.chatAdapter.setNewItems(finalList)
                }

                if (finalList.size <= 20) {
                    mBinding.chatRecycler.scrollToPosition(mViewModel.allMessages.value?.size!! - 1);

                }


            })

            showLoading.observe(this@ChatActivity, Observer {
                if (it) showProgress(this@ChatActivity) else hideProgress()
            })
        }
    }


    private fun setupScrollListener() {

        mEndlessFriendsecyclerViewScrollListener =
            object :
                EndlessChatScrollListener(mBinding.chatRecycler.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.e("ONLOADMOREMyUsers", "Onloadmore" + mViewModel.allMessages.value?.size)

                    if (mViewModel.allMessages.value!!.size % 10 == 0 && mViewModel.allMessages.value!!.size != 0) {
                        mViewModel.getAllMessages()
                    }

                }
            }



        mBinding.chatRecycler.addOnScrollListener(mEndlessFriendsecyclerViewScrollListener!!)


    }

    fun selectVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 50)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60)
        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),
            100
        )
    }


    private fun showPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
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

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {

                val selectedImageUri: Uri? = data?.data
                var filePath = PathUtil.getPath(this, selectedImageUri!!)
                if (Validator.init.validatePostMediaData(this,filePath)) {
                    videoFile = File(filePath)
                    val filePart = MultipartBody.Part.createFormData(
                        "file_name",
                        videoFile?.name,
                        RequestBody.create("video/*".toMediaTypeOrNull(), videoFile!!)

                    )

                    Log.e(
                        "FileLenghtwewrer",
                        File("${filePath}").length().toString()
                    )

                    val destPath: String =
                        DOWNLOAD_PATH + File.separator + "POST_VID_" + Calendar.getInstance().timeInMillis + ".mp4"
                    VideoCompress.compressVideoLow(
                        filePath,
                        destPath,
                        object : VideoCompress.CompressListener {
                            override fun onStart() {
                                showProgress(this@ChatActivity)
                            }

                            override fun onSuccess() {
                                hideProgress()


                                Log.e(
                                    "FileLenght",
                                    File("${DOWNLOAD_PATH + File.separator + "POST_VID_" + Calendar.getInstance().timeInMillis + ".mp4"}").length().toString()
                                )
                                sendVideoMessage(filePart)

                            }

                            override fun onFail() {

                            }

                            override fun onProgress(percent: Float) {}
                        })

                } else {

                }


            }

            if (requestCode == 101) {
                val selectedImageUri: Uri = data?.getData()!!

                var selectedImagePath = getPath(selectedImageUri)
                videoFile = File(selectedImagePath)
                if (Validator.init.validatePostMediaData(this,selectedImagePath)) {

                    val filePart = MultipartBody.Part.createFormData(
                        "file_name",
                        videoFile?.name,
                        RequestBody.create("video/*".toMediaTypeOrNull(), videoFile!!)

                    )

                    val destPath: String =
                        DOWNLOAD_PATH + File.separator + "POST_VID_" + Calendar.getInstance().timeInMillis + ".mp4"
                    VideoCompress.compressVideoLow(
                        selectedImagePath,
                        destPath,
                        object : VideoCompress.CompressListener {
                            override fun onStart() {
                                showProgress(this@ChatActivity)
                            }

                            override fun onSuccess() {
                                hideProgress()
                                sendVideoMessage(filePart)

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


    // UPDATED!
    fun getPath(uri: Uri?): String? {
        val projection =
            arrayOf(MediaStore.Video.Media.DATA)
        val cursor: Cursor =
            getContentResolver().query(uri!!, projection, null, null, null)!!
        return if (cursor != null) { // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }


    override fun onVideoMessageClick(position: Int, url: String) {
        startActivity(
            Intent(this, VideoViewActivity::class.java).putExtra(
                getString(R.string.training_videos),
                url
            )
        )
    }

    override fun onPostResume() {
        super.onPostResume()


    }


}