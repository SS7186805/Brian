package com.brian.views.fragments

import android.Manifest.permission.*
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.Prefs
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentRegisterBinding
import com.brian.internals.DialogUtil
import com.brian.internals.Utils
import com.brian.models.RegisterRequest
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.activities.AccountHandlerActivity
import com.brian.views.activities.HomeActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_register.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.File

class RegisterFragment : ScopedFragment(), KodeinAware, DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: FragmentRegisterBinding
    lateinit var mViewModel: RegisterViewModel

    //  var registerRequest = ObservableField<RegisterRequest>(RegisterRequest())
    private var datePickerDialog: DatePickerDialog? = null
    var image_path: String? = null
    var profile_pic: MultipartBody.Part? = null

    private val PERMISSION_REQUEST_CODE: Int = 101

    var list = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setupViewModel()
        setupObserver()
        mBinding = FragmentRegisterBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }


        mBinding.toolbar.tvTitle.text = getString(R.string.register)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        list.add("Male")
        list.add("Female")

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list)
        mBinding.regUserType.setAdapter(arrayAdapter)
        mBinding.regUserType.setInputType(0)


        mBinding.regUserType.setOnClickListener {
            mBinding.regUserType.showDropDown()

        }

        mBinding.regUserType.setOnFocusChangeListener { v, hasFocus ->
            mBinding.regUserType.showDropDown()
        }

        if (checkPermission()) selectImage() else requestPermission()
        return mBinding.root
    }

    private fun setupObserver() {
        mViewModel.apply {
            showLoading.observe(viewLifecycleOwner, Observer {
                if (it)
                    progress_bar.visibility = View.VISIBLE
            })

            showMessage.observe(viewLifecycleOwner, Observer {
                progress_bar.visibility = View.GONE
                if (!TextUtils.isEmpty(it))
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            })

            registerSuccess.observe(viewLifecycleOwner, Observer {
                if (it) {
                    DialogUtil.build(requireContext()) {
                        title = getString(R.string.success)
                        dialogType = DialogUtil.DialogType.SUCCESS
                        message = "success"
                        successClickListener = this@RegisterFragment
                    }
                }
            })
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    selectImage()
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {
            }
        }
    }


    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            CAMERA
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED) &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo!")
        builder.setItems(options, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, item: Int) {
                if (options[item] == "Take Photo") {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    /*  val f = File(Environment.getExternalStorageDirectory(), "temp.jpg")
                      intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))*/
                    startActivityForResult(intent, 1)
                } else if (options[item] == "Choose from Gallery") {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, 2)
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            }
        })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val photo = data?.getExtras()?.get("data") as Bitmap
                val uri_image = getImageUri(photo)
                image_path = uri_image?.let { getPath(it) }
                val file = File(image_path)
                val filePart = MultipartBody.Part.createFormData(
                    "text",
                    file.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), file)
                )
                println("filePart->$filePart")
//                mViewModel.authRequest.get()!!.profile_picture = filePart
                mViewModel.authRequest.get()!!.profile_picture = file.absolutePath
                circler_image.setImageBitmap(photo)
            } else if (requestCode == 2) {
                val SelectedImage = data?.data as Uri
                image_path = SelectedImage?.let { getPath(it) }
                val file = File(image_path)
                val filePart = MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), file)
                )
                println("filePart->$filePart")
//                mViewModel.authRequest.get()!!.profile_picture = filePart
                mViewModel.authRequest.get()!!.profile_picture = file.absolutePath

//                val selectedImage = data?.data
//                val filePath = arrayOf(MediaStore.Images.Media.DATA)
//                val c: Cursor? =
//                    selectedImage?.let {
//                        context?.getContentResolver()?.query(
//                            it,
//                            filePath,
//                            null,
//                            null,
//                            null
//                        )
//                    }
//                c?.moveToFirst()
//
//                val columnIndex: Int = c!!.getColumnIndex(filePath[0])
//                val picturePath: String = c!!.getString(columnIndex)
//                c.close()
//                val thumbnail = BitmapFactory.decodeFile(picturePath)
//                Log.w(
//                    "path of image from gallery......******************.........",
//                    picturePath + ""
//                )
                circler_image.setImageURI(SelectedImage)
            }
        }
    }

    fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(
                requireContext().contentResolver,
                inImage,
                "Title",
                null
            )
        return Uri.parse(path)
    }

    private fun getPath(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(
            requireContext().getApplicationContext(),
            contentUri,
            proj,
            null,
            null,
            null
        )
        val cursor: Cursor? = loader.loadInBackground()
        val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val result = column_index?.let { cursor.getString(it) }
        cursor?.close()
        return result
    }


    inner class ClickHandler {

        fun onclickAddImage() {
            if (checkPermission()) selectImage() else requestPermission()
        }

        fun onRegisterClick() {
            var meaasge = getString(R.string.register_message)

            if (arguments?.getString("edit").equals("edit")) {
                meaasge = getString(R.string.update_profile_message)
            }
            DialogUtil.build(requireContext()) {
                title = getString(R.string.success)
                dialogType = DialogUtil.DialogType.SUCCESS
                message = meaasge
                successClickListener = this@RegisterFragment
            }
        }

        fun selectDate() {
            hideKeyboard(requireView())
            Utils.init.selectDate(
                requireContext(),
                Utils.init.getCurrentDate(),
                mBinding.regDOB,
                false
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments?.getString("edit").equals("edit")) {
            mViewModel.isediting = true
            mBinding.regPassword.visibility = GONE
            mBinding.regCnfPassword.visibility = GONE
            mBinding.registerButton.setText(getString(R.string.update))
            mBinding.toolbar.tvTitle.setText(getString(R.string.editProfile))
            mBinding.regName.setText(getString(R.string.user_name))
            mBinding.regEmail.setText("abc@gmail.com")
            mBinding.regDOB.setText(Utils.init.getCurrentDate())
            mBinding.regDOB.visibility = GONE
            mBinding.regUserType.visibility = GONE
            mBinding.calender.visibility = GONE

        }
    }

    override fun onOkayClick() {
        if (arguments?.getString("edit").equals("edit")) {
            findNavController().navigateUp()

        } else {
            Prefs.init().isLogIn = "true"
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            (requireActivity() as AccountHandlerActivity).finish()

        }

    }
}