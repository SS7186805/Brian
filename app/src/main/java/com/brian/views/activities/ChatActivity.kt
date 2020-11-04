package com.brian.views.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brian.R
import com.brian.base.MainApplication
import com.brian.base.ScopedActivity
import com.brian.databinding.ChatFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.models.SendMessageParams
import com.brian.viewModels.messages.MessagesViewModel
import com.brian.viewModels.messages.MessagesViewModelFactory
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_main.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.*
import java.util.*


class ChatActivity : ScopedActivity(), KodeinAware {
    override val kodein by lazy { (applicationContext as KodeinAware).kodein }
    private val viewModelFactory: MessagesViewModelFactory by instance()
    lateinit var mBinding: ChatFragmentBinding
    lateinit var mViewModel: MessagesViewModel
    var chatRoomId = 0
    var otherUserId = 0
    private var videoFile: File? = null


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


        mBinding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }

        mViewModel.createChatRoom()


        setupObserver()
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
            showToast(getString(R.string.please_enter_message))
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
        mViewModel.sendMessage(messageParams)


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
                    if (mViewModel.allMessages.value?.size != 0) {
                        mViewModel.allMessages.value?.add(
                            mViewModel.allMessages.value!!.size,
                            it.data!!
                        )
                    } else {
                        mViewModel.allMessages.value?.add(
                            it.data!!
                        )
                    }

                    mBinding.chatRecycler.scrollToPosition(mViewModel.allMessages.value?.size!! - 1)
                }

            })

            allMessages.observe(this@ChatActivity, Observer {

                Collections.reverse(it)
                mViewModel.chatAdapter.updateList(it)
                mBinding.chatRecycler.scrollToPosition(mViewModel.allMessages.value?.size!! - 1);


            })

            showLoading.observe(this@ChatActivity, Observer {
                if (it) showProgress(this@ChatActivity) else hideProgress()
            })
        }
    }


    fun selectVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
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
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                val selectedImageUri: Uri? = data?.data

                Log.e("selectImageApUri", "Uri${selectedImageUri}")
                Log.e("selectImageApUriPath", "Uri${selectedImageUri?.path}")

                var id = DocumentsContract.getDocumentId(selectedImageUri);
                var inputStream = getContentResolver().openInputStream(
                    selectedImageUri!!
                );

                var file = File(cacheDir.getAbsolutePath() + "/" + id);

                writeFile(inputStream!!, file);
                var filePath = file.getAbsolutePath();

                videoFile = File(filePath)
                val filePart = MultipartBody.Part.createFormData(
                    "file_name",
                    videoFile?.name,
                    RequestBody.create("video/*".toMediaTypeOrNull(), videoFile!!)

                )

                sendVideoMessage(filePart)

                Log.e("ImageUri", "####${selectedImageUri}")


            }

            if (requestCode == 101) {
                val selectedImageUri: Uri = data?.getData()!!

                var selectedImagePath = getPath(selectedImageUri)
                videoFile = File(selectedImagePath)
                val filePart = MultipartBody.Part.createFormData(
                    "file_name",
                    videoFile?.name,
                    RequestBody.create("video/*".toMediaTypeOrNull(), videoFile!!)

                )
                sendVideoMessage(filePart)


                Log.e("ImageUri", "####${selectedImageUri}")


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
            getContentResolver().query(uri!!, projection, null, null, null)!!
        return if (cursor != null) { // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }


}